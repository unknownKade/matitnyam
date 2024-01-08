package com.wandted.matitnyam.user.domain;

import com.wandted.matitnyam.user.dto.UserCreateRequest;
import com.wandted.matitnyam.user.dto.UserDetailResponse;
import com.wandted.matitnyam.user.dto.UserUpdateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class CustomUser {

    @Id
    private String id;

    @Comment("사용자명")
    private String username;

    @Comment("비밀번호")
    @Column(nullable = false)
    private String password;

    @Comment("주소")
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    @Comment("위도")
    private Long latitude;

    @Comment("경도")
    @Column(nullable = false)
    private Long longitude;

    @Enumerated(EnumType.STRING)
    @Comment("권한")
    @Column(nullable = false)
    private Role role;

    @Comment("점심추천여부")
    @Column(nullable = false)
    private Boolean useRecommendLunch;

    @Comment("사용여부")
    @Column(nullable = false)
    private Boolean isActive;

    public static CustomUser create(UserCreateRequest userCreateRequest) {
        return CustomUser.builder()
                .id(userCreateRequest.id())
                .password(userCreateRequest.password())
                .address(userCreateRequest.address())
                .latitude(userCreateRequest.latitude())
                .longitude(userCreateRequest.logitude())
                .role(userCreateRequest.role())
                .useRecommendLunch(userCreateRequest.useRecommendLunch())
                .isActive(true)
                .build();
    }

    public UserDetailResponse toDto(){
        return new UserDetailResponse(id, username, address, useRecommendLunch);
    }

    public void updateUser(UserUpdateRequest userUpdateRequest){
        this.username = userUpdateRequest.username();
        this.address = userUpdateRequest.address();
        this.latitude = userUpdateRequest.latitude();
        this.longitude = userUpdateRequest.longitude();
        this.role = userUpdateRequest.role();
        this.useRecommendLunch = userUpdateRequest.useRecommendLunch();
    }
}
