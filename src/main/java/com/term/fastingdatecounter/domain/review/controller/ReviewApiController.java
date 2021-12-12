package com.term.fastingdatecounter.domain.review.controller;

import com.term.fastingdatecounter.domain.review.dto.ReviewListResponse;
import com.term.fastingdatecounter.domain.review.dto.ReviewRequest;
import com.term.fastingdatecounter.domain.review.domain.Review;
import com.term.fastingdatecounter.domain.review.dto.ReviewResponse;
import com.term.fastingdatecounter.domain.review.service.ReviewService;
import com.term.fastingdatecounter.domain.user.dto.SessionUser;
import com.term.fastingdatecounter.domain.user.domain.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


import javax.validation.Valid;
import java.net.URI;
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
            @LoginUser SessionUser user,
            @PathVariable(name = "foodId") Long foodId
    ){
        List<Review> reviews = reviewService.findByFoodId(user.getId(), foodId);
        return ResponseEntity.ok(new ReviewListResponse(reviews));
    }

    @Operation(summary = "리뷰 등록")
    @PostMapping
    public ResponseEntity<ReviewResponse> save(
            UriComponentsBuilder uriBuilder,
            @LoginUser SessionUser user,
            @PathVariable(name = "foodId") Long foodId,
            @Valid @RequestBody ReviewRequest reviewRequest
    ){
        URI uri = uriBuilder.path("/api/food/{foodId}/reviews").buildAndExpand(foodId).toUri();
        Review review = reviewService.save(user.getId(), foodId, reviewRequest);
        return ResponseEntity.created(uri).body(new ReviewResponse(review));
    }

    @Operation(summary = "리뷰 수정")
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> update(
            @LoginUser SessionUser user,
            @PathVariable(name = "foodId") Long foodId,
            @PathVariable(name = "reviewId") Long reviewId,
            @Valid @RequestBody ReviewRequest reviewRequest
    ){
        Review review = reviewService.update(user.getId(), foodId, reviewId, reviewRequest);
        return ResponseEntity.ok(new ReviewResponse(review));
    }

    @Operation(summary = "리뷰 삭제")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> delete(
            @LoginUser SessionUser user,
            @PathVariable(name = "foodId") Long foodId,
            @PathVariable(name = "reviewId") Long reviewId
    ){
        reviewService.delete(user.getId(), foodId, reviewId);
        return ResponseEntity.noContent().build();
    }

}
