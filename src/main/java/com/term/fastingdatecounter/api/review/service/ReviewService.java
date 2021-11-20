package com.term.fastingdatecounter.api.review.service;

import com.term.fastingdatecounter.api.review.controller.dto.ReviewRequest;
import com.term.fastingdatecounter.api.review.domain.Review;
import com.term.fastingdatecounter.api.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> findByFoodId(Long foodId) {
        return reviewRepository.findByFoodId(foodId);
    }

    public void save(Long foodId, ReviewRequest reviewRequest){}
    public void update(Long reviewId, ReviewRequest reviewRequest){}
    public void delete(Long reviewId){}
}
