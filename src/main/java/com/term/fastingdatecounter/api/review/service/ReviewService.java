package com.term.fastingdatecounter.api.review.service;

import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.repository.FoodRepository;
import com.term.fastingdatecounter.api.review.controller.dto.ReviewRequest;
import com.term.fastingdatecounter.api.review.domain.Review;
import com.term.fastingdatecounter.api.review.repository.ReviewRepository;
import com.term.fastingdatecounter.api.user.domain.User;
import com.term.fastingdatecounter.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public Review findById(Long id){
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ServiceException("리뷰를 찾을 수 없습니다"));
    }

    @Transactional(readOnly = true)
    public List<Review> findByFoodId(Long foodId) {
        return reviewRepository.findByFoodIdOrderByDateDesc(foodId);
    }

    @Transactional
    public Review save(Long userId, Long foodId, ReviewRequest reviewRequest){
        User user = findUserById(userId); // needs to validate
        Food food = findFoodById(foodId); // needs to validate
        Review review = reviewRequest.toEntity(food);
        reviewRepository.save(review);
        return review;
    }

    @Transactional
    public Review update(Long userId, Long reviewId, ReviewRequest reviewRequest){
        User user = findUserById(userId); // needs to implement validation
        Review review = findReviewById(reviewId);
        review.update(reviewRequest.getDate(), reviewRequest.getTitle(), reviewRequest.getContent(), reviewRequest.isFasted());
        return review;
    }

    @Transactional
    public void delete(Long userId, Long reviewId){
        User user = findUserById(userId);
        Review review = findReviewById(reviewId);
        reviewRepository.delete(review);
    }

    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("계정을 찾을 수 없습니다."));
        return user;
    }

    public Food findFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ServiceException("음식을 찾을 수 없습니다."));
        return food;
    }

    public Review findReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException("리뷰를 찾을 수 없습니다."));
        return review;
    }
}
