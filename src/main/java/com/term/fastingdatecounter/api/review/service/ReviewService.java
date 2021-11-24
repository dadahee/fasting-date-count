package com.term.fastingdatecounter.api.review.service;

import com.term.fastingdatecounter.api.review.controller.dto.ReviewRequest;
import com.term.fastingdatecounter.api.review.domain.Review;
import com.term.fastingdatecounter.api.review.repository.ReviewRepository;
import com.term.fastingdatecounter.api.user.controller.dto.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Review findById(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ServiceException("리뷰를 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public List<Review> findByFoodId(Long foodId) {
        return reviewRepository.findByFoodId(foodId);
    }

    @Transactional
    public void save(Long userId, Long foodId, ReviewRequest reviewRequest){}

    @Transactional
    public void update(Long userId, Long reviewId, ReviewRequest reviewRequest){}

    @Transactional
    public void delete(Long userId, Long reviewId){}
}
