package com.term.fastingdatecounter.api.review.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.api.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
public class ReviewRequest {

    private Date date;
    private String title;
    private String content;
    private boolean fasted;

    public ReviewRequest(Review review) {
        this.date = review.getDate();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.fasted = review.isFasted();
    }

    public Review toEntity(){
        return Review.builder() // 유저 처리 필요
                .date(date)
                .title(title)
                .content(content)
                .fasted(fasted)
                .build();
    }
}
