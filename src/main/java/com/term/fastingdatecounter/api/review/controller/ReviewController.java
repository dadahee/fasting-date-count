package com.term.fastingdatecounter.api.review.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.service.FoodService;
import com.term.fastingdatecounter.api.review.controller.dto.ReviewListResponse;
import com.term.fastingdatecounter.api.review.controller.dto.ReviewResponse;
import com.term.fastingdatecounter.api.review.domain.Review;
import com.term.fastingdatecounter.api.review.service.ReviewService;
import com.term.fastingdatecounter.api.user.controller.dto.dto.SessionUser;
import com.term.fastingdatecounter.api.user.domain.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "리뷰 관련 페이지")
@RequiredArgsConstructor
@RequestMapping("/food/{foodId}/reviews")
@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final FoodService foodService;

    @Operation(summary = "리뷰 목록 페이지")
    @GetMapping
    public String review(
            Model model,
            @PathVariable(name = "foodId") Long foodId,
            @LoginUser SessionUser user
    ){
        List<Review> reviewList = reviewService.findByFoodId(foodId); // find review list by session user id
        List<ReviewResponse> reviewListResponse = reviewList.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
        Food food = foodService.findById(foodId);
        model.addAttribute("foodId", foodId); // 거슬려..
        model.addAttribute("foodName", food.getName());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("reviewList",reviewList);
        return "review";
    }

    @Operation(summary = "리뷰 작성 페이지")
    @GetMapping("/save")
    public String reviewSaveForm(
            Model model,
            @PathVariable(name = "foodId") Long foodId
    ) {
        model.addAttribute("foodId", foodId);
        return "review-save";
    }

    @Operation(summary = "리뷰 수정 페이지")
    @GetMapping("/update/{reviewId}")
    public String reviewUpdateForm(
            Model model,
            @PathVariable(name = "foodId") Long foodId,
            @PathVariable(name = "reviewId") Long reviewId
    ){
        Review review = reviewService.findById(reviewId);
        model.addAttribute("id", reviewId);
        model.addAttribute("foodId", foodId);
//        model.addAttribute("review", new Gson().toJson(review));
        model.addAttribute("date", review.getDate());
        model.addAttribute("title", review.getTitle());
        model.addAttribute("content", review.getContent());
        model.addAttribute("fasted", review.isFasted());
        return "review-update";
    }
}
