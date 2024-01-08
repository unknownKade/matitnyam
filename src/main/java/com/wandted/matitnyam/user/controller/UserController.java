package com.wandted.matitnyam.user.controller;

import com.wandted.matitnyam.user.domain.CustomUser;
import com.wandted.matitnyam.user.dto.UserCreateRequest;
import com.wandted.matitnyam.user.dto.UserDetailResponse;
import com.wandted.matitnyam.user.dto.UserUpdateRequest;
import com.wandted.matitnyam.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDetailResponse getUserDetail(@AuthenticationPrincipal CustomUser customUser){
        return userService.getUserDetail(customUser.getId());
    }

    @PutMapping
    public ResponseEntity<Void> updateUserDetail(@AuthenticationPrincipal CustomUser customUser, @RequestBody UserUpdateRequest userUpdateRequest){
        if(!customUser.getId().equals(userUpdateRequest.id())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .build();
        }

        userService.updateUserDetail(userUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> createUserDetail(@RequestBody UserCreateRequest userCreateRequest){
        userService.createUserDetail(userCreateRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

}
