package com.term.fastingdatecounter.api.food.service;

import com.term.fastingdatecounter.api.food.controller.dto.FoodRequest;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.repository.FoodRepository;
import com.term.fastingdatecounter.api.review.domain.Review;
import com.term.fastingdatecounter.api.review.repository.ReviewRepository;
import com.term.fastingdatecounter.api.user.domain.User;
import com.term.fastingdatecounter.api.user.repository.UserRepository;
import com.term.fastingdatecounter.global.exception.ErrorCode;
import com.term.fastingdatecounter.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final UserRepository userRepository;
    private final FoodRepository foodRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public List<Food> findByUserId(Long userId) {
        return foodRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Food findById(Long id) {
        return findFoodById(id);
    }

    @Transactional
    public Food save(Long userId, FoodRequest foodRequest) {
        User user = findUserById(userId);
        Food food = foodRequest.toEntity(user);
        foodRepository.save(food);
        return food;
    }

    @Transactional
    public Food update(Long userId, Long foodId, FoodRequest foodRequest) {
        User user = findUserById(userId);
        Food food = findFoodById(foodId);
        validateUserAuthority(user.getId(), food.getUser().getId());
        food.updateName(foodRequest.getName());
        validateStartDate(foodId, foodRequest.getStartDate());
        food.updateStartDate(foodRequest.getStartDate());
        return food;
    }


    @Transactional
    public void delete(Long userId, Long foodId) {
        User user = findUserById(userId);
        Food food = findFoodById(foodId);
        validateUserAuthority(user.getId(), food.getUser().getId());
        foodRepository.delete(food);
    }

    @Transactional
    public Food updateDayCount(Food food){
        Long fastingCount = reviewRepository.countByFoodIdAndFastedIsTrue(food.getId());
        food.updateDayCount(fastingCount);
        return food;
    }

    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_USER));
        return user;
    }

    public Food findFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_FOUND_FOOD));
        return food;
    }

    private void validateUserAuthority(Long userId, Long extractedId) {
        // 세션유저 정보 != 음식에 저장된 유저 정보일 경우 error
        if (!userId.equals(extractedId)) {
            throw new ServiceException(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED);
        }
    }

    private void validateStartDate(Long foodId, LocalDate startDate) {
        // 음식에 등록된 리뷰 중 가장 빠른 날짜보다 시작 날짜가 늦으면 error
        List<Review> reviews = reviewRepository.findByFoodIdOrderByDateDesc(foodId);
        if (! reviews.isEmpty()) {
            Review firstReview = reviews.get(reviews.size() - 1);
            if (startDate.isAfter(firstReview.getDate())) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.TOO_LATE_FOOD_START_DATE);
            }
        }
    }
}
