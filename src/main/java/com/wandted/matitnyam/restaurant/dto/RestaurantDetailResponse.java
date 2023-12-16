package com.wandted.matitnyam.restaurant.dto;

import com.wandted.matitnyam.region.domain.District;
import com.wandted.matitnyam.review.dto.ReviewResponse;
import java.util.List;

public record RestaurantDetailResponse(
    Long id,
    String name,
    District district,
    String address,
    Double latitude,
    Double longitude,
    List<ReviewResponse> reviews
) {
}
