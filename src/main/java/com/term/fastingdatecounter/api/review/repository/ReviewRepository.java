package com.term.fastingdatecounter.api.review.repository;

import com.term.fastingdatecounter.api.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
