package com.wandted.matitnyam.review.repository;

import com.wandted.matitnyam.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
