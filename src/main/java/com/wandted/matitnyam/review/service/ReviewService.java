package com.wandted.matitnyam.review.service;

import com.wandted.matitnyam.common.DataException.NoDataException;
import com.wandted.matitnyam.restaurant.domain.Restaurant;
import com.wandted.matitnyam.restaurant.repository.RestaurantRepository;
import com.wandted.matitnyam.review.domain.Review;
import com.wandted.matitnyam.review.dto.ReviewCreateRequest;
import com.wandted.matitnyam.review.dto.ReviewUpdateRequest;
import com.wandted.matitnyam.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;

    public void createReview(ReviewCreateRequest reviewCreateRequest){
        Restaurant restaurant = restaurantRepository.findById(reviewCreateRequest.restaurantId())
                        .orElseThrow(() -> new NoDataException(reviewCreateRequest.restaurantId().toString()));

        reviewRepository.save(
                Review.builder()
                        .restaurant(restaurant)
                        .rate(reviewCreateRequest.rate())
                        .comment(reviewCreateRequest.comment())
                        .build()
        );
    }

    public void updateReview(ReviewUpdateRequest reviewUpdateRequest){
        Review review = reviewRepository.findById(reviewUpdateRequest.id())
                .orElseThrow(() -> new NoDataException(reviewUpdateRequest.id().toString()));
        review.update(reviewUpdateRequest.comment());
    }
}
