package com.wandted.matitnyam.review.dto;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record ReviewResponse(
        Long id,
        Double rate,
        String comment,
        String userId,
        LocalDateTime editedDate,
        Boolean isModified
) {
}
