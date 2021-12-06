package com.term.fastingdatecounter.api.review.repository;

import com.term.fastingdatecounter.api.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByFoodIdOrderByDateDesc(Long foodId);
    Long countByFoodIdAndDate(Long foodId, LocalDate date);

    Long countByFoodIdAndFastedIsTrue(Long foodId);
}
