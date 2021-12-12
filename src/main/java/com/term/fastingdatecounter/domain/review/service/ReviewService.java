package com.term.fastingdatecounter.domain.review.service;

import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.food.repository.FoodRepository;
import com.term.fastingdatecounter.domain.food.service.FoodService;
import com.term.fastingdatecounter.domain.review.dto.ReviewRequest;
import com.term.fastingdatecounter.domain.review.domain.Review;
import com.term.fastingdatecounter.domain.review.repository.ReviewRepository;
import com.term.fastingdatecounter.domain.user.domain.User;
import com.term.fastingdatecounter.domain.user.repository.UserRepository;
import com.term.fastingdatecounter.global.exception.ErrorCode;
import com.term.fastingdatecounter.global.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    private final FoodService foodService;

    @Transactional(readOnly = true)
    public Review findById(Long id){
        return findReviewById(id);
    }

    @Transactional(readOnly = true)
    public List<Review> findByFoodId(Long userId, Long foodId) {
        Food food = findFoodById(foodId);
        validateUserAuthority(userId, food.getUser().getId());
        return reviewRepository.findByFoodIdOrderByDateDesc(food.getId());
    }

    @Transactional
    public Review save(Long userId, Long foodId, ReviewRequest reviewRequest){
        // 유저, 음식 정보 불러오고, review 엔티티화
        User user = findUserById(userId);
        Food food = findFoodById(foodId);
        Review review = reviewRequest.toEntity(food);

        // 유저 권한 확인 및 리뷰 등록일 유효성 체크
        validateUserAuthority(user.getId(), food.getUser().getId());
        validateReviewDate(food, reviewRequest.getDate());

        // 리뷰 등록 및 음식의 단식일수 업데이트
        reviewRepository.save(review);
        foodService.updateDayCount(food);
        return review;
    }

    @Transactional
    public Review update(Long userId, Long foodId, Long reviewId, ReviewRequest reviewRequest){
        // 유저, 음식, 리뷰 불러오기
        User user = findUserById(userId);
        Review review = findReviewById(reviewId);
        Food food = findFoodById(foodId);

        // 유저 권한 확인 및 리뷰 등록일 유효성 체크
        validateUserAuthority(food.getUser().getId(), review.getFood().getUser().getId());
        validateUserAuthority(user.getId(), food.getUser().getId());
        validateReviewDate(food, reviewId, reviewRequest.getDate());

        // 리뷰 업데이트 및 음식의 단식일수 업데이트
        review.updateReview(reviewRequest.getDate(), reviewRequest.getTitle(), reviewRequest.getContent(), reviewRequest.isFasted());
        foodService.updateDayCount(food);
        return review;
    }

    @Transactional
    public void delete(Long userId, Long foodId, Long reviewId){
        // 유저, 음식, 리뷰 불러오기
        User user = findUserById(userId);
        Review review = findReviewById(reviewId);
        Food food = findFoodById(foodId);

        // 유저 권한 확인
        validateUserAuthority(food.getUser().getId(), review.getFood().getUser().getId());
        validateUserAuthority(user.getId(), food.getUser().getId());

        // 리뷰 업데이트 및 음식의 단식일수 업데이트
        reviewRepository.delete(review);
        foodService.updateDayCount(food);
    }

    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_USER));
        return user;
    }

    public Food findFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_FOOD));
        return food;
    }

    public Review findReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND_REVIEW));
        return review;
    }

    private void validateUserAuthority(Long userId, Long extractedId) {
        // 세션유저 정보 != 음식에 저장된 유저 정보일 경우 error
        if (!userId.equals(extractedId)) {
            throw new ServiceException(HttpStatus.FORBIDDEN, ErrorCode.ACCESS_DENIED);
        }
    }

    public void validateReviewDate(Food food, LocalDate reviewDate) {
        // 음식 단식 시작일 리뷰 날짜가 이르면
        if (reviewDate.isBefore(food.getStartDate())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.EARLIER_REVIEW_DATE);
        }
        // 음식의 해당 날짜에 이미 다른 작성된 리뷰가 있으면
        Optional<Review> review = reviewRepository.findByFoodIdAndDate(food.getId(), reviewDate);
        if (review.isPresent()) {
            throw new ServiceException(HttpStatus.CONFLICT, ErrorCode.ALREADY_WRITTEN_REVIEW_DATE);
        }
    }

    public void validateReviewDate(Food food, Long reviewId, LocalDate reviewDate){
        // 음식 단식 시작일 리뷰 날짜가 이르면
        if (reviewDate.isBefore(food.getStartDate())) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, ErrorCode.EARLIER_REVIEW_DATE);
        }
        // 음식의 해당 날짜에 이미 다른 작성된 리뷰가 있으면
        Optional<Review> review = reviewRepository.findByFoodIdAndDate(food.getId(), reviewDate);
        if (review.isPresent() && review.get().getId() != reviewId) {
            throw new ServiceException(HttpStatus.CONFLICT, ErrorCode.ALREADY_WRITTEN_REVIEW_DATE);
        }
    }
}
