package com.term.fastingdatecounter.api.review.controller;

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

@Tag(name = "리뷰 관련 페이지")
@RequiredArgsConstructor
@RequestMapping("/food/{foodId}/reviews")
@Controller
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 목록 페이지")
    @GetMapping
    public String review(Model model, @LoginUser SessionUser user){
        List<Review> reviewList = reviewService.findByFoodId(user.getId()); // find review list by session user id
        model.addAttribute("user", user);
        model.addAttribute("reviewList", new ReviewListResponse(reviewList));
        return "review";
    }

    @Operation(summary = "리뷰 작성 페이지")
    @GetMapping("/save")
    public String reviewSaveForm() {
        return "review-save";
    }

    @Operation(summary = "리뷰 수정 페이지")
    @GetMapping("/update/{reviewId}")
    public String reviewUpdateForm(Model model, @PathVariable Long reviewId){
        Review review = reviewService.findById(reviewId);
        model.addAttribute("review", new ReviewResponse(review));
        return "review-update";
    }
}
