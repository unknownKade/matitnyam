package com.wandted.matitnyam.user.service;

import com.wandted.matitnyam.security.token.Token;
import com.wandted.matitnyam.security.token.TokenProvider;
import com.wandted.matitnyam.user.domain.CustomUser;
import com.wandted.matitnyam.user.dto.LoginRequest;
import com.wandted.matitnyam.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    //로그인시 새 RefreshToken, AccessToken 발급
    public Token handleLogin(LoginRequest loginRequest){
        CustomUser customUser = loadUserById(loginRequest.id());

        if(!passwordEncoder.matches(loginRequest.password(), customUser.getPassword())){
            throw new BadCredentialsException(loginRequest.id());
        }

        return generateNewToken(customUser);
    }

    //RefreshToken대조 후 AccessToken 재발급
    public String handleRefresh(Token token){
        String userId = tokenProvider.getUserId(token.getRefreshToken());

        if(!checkRefreshToken(userId, token.getRefreshToken())){
            throw new BadCredentialsException(token.getRefreshToken());
        }

        CustomUser customUser = loadUserById(userId);
        return tokenProvider.generateToken(customUser, true);
    }

    //로그아웃시 RefreshToken 삭제
    public void handleLogout(Token token){
        try{
            tokenProvider.getUserId(token.getRefreshToken());
        }catch (ExpiredJwtException e){
            redisTemplate.delete(e.getClaims().getId());
        }
    }

    private CustomUser loadUserById(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    private boolean checkRefreshToken(String userId, String refreshToken){
        return redisTemplate.opsForValue()
                .get(userId)
                .equals(refreshToken);
    }

    private Token generateNewToken(CustomUser customUser){
        Token token = new Token();

        token.setAccessToken(tokenProvider.generateToken(customUser, true));
        token.setRefreshToken(tokenProvider.generateToken(customUser, false));

        redisTemplate.opsForValue()
                .set(customUser.getId(), token.getRefreshToken());

        return token;
    }
}
