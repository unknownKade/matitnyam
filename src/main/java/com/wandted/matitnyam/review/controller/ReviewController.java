package com.wandted.matitnyam.review.controller;

import com.wandted.matitnyam.review.dto.ReviewCreateRequest;
import com.wandted.matitnyam.review.dto.ReviewUpdateRequest;
import com.wandted.matitnyam.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public void createReview(ReviewCreateRequest reviewCreateRequest){
        reviewService.createReview(reviewCreateRequest);
    }

    @PutMapping
    public void updateReview(ReviewUpdateRequest reviewUpdateRequest){
        reviewService.updateReview(reviewUpdateRequest);
    }


}
