package com.wandted.matitnyam.review.dto;

public record ReviewCreateRequest(
        Long restaurantId,
        String comment,
        Double rate
) {
}
