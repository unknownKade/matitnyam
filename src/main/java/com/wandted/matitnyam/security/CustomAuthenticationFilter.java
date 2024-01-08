package com.wandted.matitnyam.security;

import com.wandted.matitnyam.security.token.Token;
import com.wandted.matitnyam.security.token.TokenCode;
import com.wandted.matitnyam.security.token.TokenProvider;
import com.wandted.matitnyam.user.service.UserLoginService;
import com.wandted.matitnyam.util.SetCookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final UserLoginService userLoginService;

    private final String loginURI = "/user/login";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean isLogin = request.getRequestURI().equals(loginURI) && request.getMethod().equals(HttpMethod.POST.name());

        //로그인 외 요청에서 토큰 체크
//        if(!isLogin){
            Token token = getTokenFromRequest(request);
            TokenCode tokenCode = tokenProvider.validateToken(token.getAccessToken());

            //AccessToken이 유효한 경우 로그인
            if (tokenCode == TokenCode.VALID_TOKEN) {
                CustomAuthenticationToken authenticationToken = tokenProvider.getUserDetail(token.getAccessToken());

                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext()
                        .setAuthentication(authenticationToken);
            }

            //AccessToken이 만료된 경우 RefreshToken 확인
            if (tokenCode == TokenCode.EXPIRED_TOKEN){
                if(tokenProvider.validateToken(token.getRefreshToken()).equals(TokenCode.VALID_TOKEN)){
                    response.addCookie(SetCookieUtil.setTokenCookie(userLoginService.handleRefresh(token), true));
                }else{
                    userLoginService.handleLogout(token);
                }
            }
//        }

        filterChain.doFilter(request, response);
    }

    //토큰 쿠키를 찾는다
    private Token getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Token token = new Token();

        if (cookies == null){
            throw new IllegalArgumentException("token is null");
        }

        for(Cookie cookie : cookies){
            if (cookie.getName().equals(HttpProperties.ACCESS_TOKEN_COOKIE))
                token.setAccessToken(cookie.getValue());

            if (cookie.getName().equals(HttpProperties.REFRESH_TOKEN_COOKIE))
                token.setRefreshToken(cookie.getValue());
        }

        if(token.getAccessToken().isEmpty() || token.getRefreshToken().isEmpty()){
            throw new IllegalArgumentException("token is null");
        }

        return token;
    }
}
