package com.wandted.matitnyam.security.token;

import com.wandted.matitnyam.security.CustomAuthenticationToken;
import com.wandted.matitnyam.user.domain.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TokenProvider {
    //access 토큰 만료 여부 체크
    public TokenCode validateToken(String token) {
        try {
            return getAllClaims(token)
                    .getBody()
                    .getAudience()
                    .equals(TokenProperties.TOKEN_AUDIENCE) ? TokenCode.VALID_TOKEN : TokenCode.INVALID_TOKEN;
        }catch (ExpiredJwtException e){
            return TokenCode.EXPIRED_TOKEN;
        }catch (MalformedJwtException e){
            return TokenCode.INVALID_TOKEN;
        }
    }

    //AccessToken의 사용자 정보를 SecurityContext에 넣을 수 있도록 세팅
    public CustomAuthenticationToken getUserDetail(String accessToken){
        Claims claims = getAllClaims(accessToken).getBody();

        return new CustomAuthenticationToken(
                claims.getSubject(),
                claims.get("username").toString(),
                Long.parseLong(claims.get("latitude").toString()),
                Long.parseLong(claims.get("longitude").toString()),
                Boolean.valueOf(claims.get("recommend").toString()),
                List.of(new SimpleGrantedAuthority(claims.get("role").toString()))
        );
    }

    //RefreshToken에서 id추출
    public String getUserId(String refreshToken){
        return getAllClaims(refreshToken).getBody().getSubject();
    }

    //토큰에 담겨진 사용자 정보를 추출
    private Jws<Claims> getAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token);
    }

    //토큰에 필요한 사용자 정보 담기
    public String generateToken(CustomUser customUser, boolean isAccessToken){
        Claims claims = Jwts.claims()
                .setAudience(TokenProperties.TOKEN_AUDIENCE)
                .setSubject(customUser.getId())
                .setExpiration(Date.from(
                        Instant
                                .now()
                                .plus( isAccessToken? TokenProperties.ACCESS_TOKEN_DURATION: TokenProperties.REFRESH_TOKEN_DURATION,
                                        ChronoUnit.HOURS)
                ));

        if(isAccessToken){
            claims.put("username", customUser.getUsername());
            claims.put("role", customUser.getRole());
            claims.put("longitude", customUser.getLongitude());
            claims.put("latitude", customUser.getLatitude());
            claims.put("recommend", customUser.getUseRecommendLunch());
        }

        return Jwts.builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // secretKey로 서명 키 만든다
    private Key getSignInKey() {
        byte[] keyBytes = TokenProperties.SECRET_KEY
                .getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
