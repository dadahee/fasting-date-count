package com.term.fastingdatecounter.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.domain.review.domain.Review;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReviewResponse {

    private final Long id;
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private final LocalDate date;
    private final String title;
    private final String content;
    private final boolean fasted;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.date = review.getDate();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.fasted = review.isFasted();
    }
}
