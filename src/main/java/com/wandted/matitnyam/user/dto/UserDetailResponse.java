package com.wandted.matitnyam.user.dto;

public record UserDetailResponse(
        String id,
        String username,
        String address,
        Boolean useRecommendLunch
) {
}
