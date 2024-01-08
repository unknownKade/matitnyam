package com.wandted.matitnyam.user.dto;

import com.wandted.matitnyam.user.domain.Role;

public record UserCreateRequest(
        String id,
        String username,
        String password,
        String address,
        Long latitude,
        Long logitude,
        Role role,
        Boolean useRecommendLunch
) {
}
