package com.term.fastingdatecounter.api.review.controller;

import com.term.fastingdatecounter.api.review.controller.dto.ReviewListResponse;
import com.term.fastingdatecounter.api.review.controller.dto.ReviewRequest;
import com.term.fastingdatecounter.api.review.domain.Review;
import com.term.fastingdatecounter.api.review.service.ReviewService;
import com.term.fastingdatecounter.api.user.controller.dto.dto.SessionUser;
import com.term.fastingdatecounter.api.user.domain.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Tag(name = "리뷰(Review) API")
@RequiredArgsConstructor
@RequestMapping("/api/food/{foodId}/reviews")
@RestController
public class ReviewApiController {

    private final ReviewService reviewService;

    @Operation(summary = "리뷰 목록 조회")
    @GetMapping
    public ResponseEntity<ReviewListResponse> find(
            @PathVariable(name = "foodId") Long foodId
    ){
        List<Review> reviews = reviewService.findByFoodId(foodId);
        return ResponseEntity.ok(new ReviewListResponse(reviews));
    }

    @Operation(summary = "리뷰 등록")
    @PostMapping
    public ResponseEntity<Void> save(
            @LoginUser SessionUser user,
            @PathVariable(name = "foodId") Long foodId,
            @RequestBody ReviewRequest reviewRequest
    ){
        reviewService.save(user.getId(), foodId, reviewRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "리뷰 수정")
    @PutMapping("/{reviewId}")
    public ResponseEntity<Void> update(
            @LoginUser SessionUser user,
            @PathVariable(name = "reviewId") Long reviewId,
            @RequestBody ReviewRequest reviewRequest
    ){
        reviewService.update(user.getId(), reviewId, reviewRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(
            @LoginUser SessionUser user,
            @PathVariable(name = "reviewId") Long reviewId
    ){
        reviewService.delete(user.getId(), reviewId);
        return ResponseEntity.noContent().build();
    }

}
