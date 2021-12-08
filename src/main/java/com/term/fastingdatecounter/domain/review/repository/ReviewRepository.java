package com.term.fastingdatecounter.domain.review.repository;

import com.term.fastingdatecounter.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByFoodIdOrderByDateDesc(Long foodId);
    Optional<Review> findByFoodIdAndDate(Long foodId, LocalDate date);

    Long countByFoodIdAndFastedIsTrue(Long foodId);
}
