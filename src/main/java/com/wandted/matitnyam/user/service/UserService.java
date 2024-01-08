package com.wandted.matitnyam.user.service;

import com.wandted.matitnyam.common.DataException.NoDataException;
import com.wandted.matitnyam.user.domain.CustomUser;
import com.wandted.matitnyam.user.dto.UserCreateRequest;
import com.wandted.matitnyam.user.dto.UserDetailResponse;
import com.wandted.matitnyam.user.dto.UserUpdateRequest;
import com.wandted.matitnyam.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserDetailResponse getUserDetail(String userId){
        return userRepository.findByIdAndIsActiveIsTrue(userId)
                .orElseThrow(()-> new NoDataException("사용자 정보가 없습니다"))
                .toDto();
    }

    public void updateUserDetail(UserUpdateRequest userUpdateRequest){
        userRepository.findByIdAndIsActiveIsTrue(userUpdateRequest.id())
                .orElseThrow(()-> new NoDataException("사용자 정보가 없습니다"))
                .updateUser(userUpdateRequest);
    }

    public void createUserDetail(UserCreateRequest userCreateRequest){
        userRepository.findById(userCreateRequest.id())
                .ifPresent( user -> {
                    throw new NoDataException("유효하지 않은 ID 입니다");
                });

        userRepository.save(CustomUser.create(userCreateRequest));
    }
}
