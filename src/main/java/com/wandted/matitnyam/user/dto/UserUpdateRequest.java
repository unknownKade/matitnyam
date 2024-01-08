package com.wandted.matitnyam.user.dto;

import com.wandted.matitnyam.user.domain.Role;

public record UserUpdateRequest(
        String id,
        String username,
        String address,
        Long latitude,
        Long longitude,
        Role role,
        Boolean useRecommendLunch
) {
}
