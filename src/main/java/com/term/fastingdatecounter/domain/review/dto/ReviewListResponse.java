package com.term.fastingdatecounter.domain.review.dto;

import com.term.fastingdatecounter.domain.review.domain.Review;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReviewListResponse {

    private final List<ReviewResponse> reviewList;

    public ReviewListResponse(List<Review> reviewList) {
        this.reviewList = reviewList.stream()
                .map(ReviewResponse::new)
                .collect(Collectors.toList());
    }
}
