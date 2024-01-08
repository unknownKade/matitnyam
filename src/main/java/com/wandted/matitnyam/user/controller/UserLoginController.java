package com.wandted.matitnyam.user.controller;

import com.wandted.matitnyam.security.token.Token;
import com.wandted.matitnyam.user.dto.LoginRequest;
import com.wandted.matitnyam.user.service.UserLoginService;
import com.wandted.matitnyam.util.SetCookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("user")
@RestController
public class UserLoginController {

    private final UserLoginService userLoginService;

    @PostMapping("login")
    public ResponseEntity<Token> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        Token token = userLoginService.handleLogin(loginRequest);

        response.addCookie(SetCookieUtil.setTokenCookie(token.getAccessToken(), true));
        response.addCookie(SetCookieUtil.setTokenCookie(token.getRefreshToken(), false));

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

}
