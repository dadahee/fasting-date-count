package com.term.fastingdatecounter.api.review.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ReviewRequest {

    @PastOrPresent(message = "FUTURE_REVIEW_DATE") // not working
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank(message = "EMPTY_REVIEW_TITLE")
    @Size(min = 1, max = 100, message = "TOO_LONG_REVIEW_TITLE")
    private String title;

    @NotNull(message = "EMPTY_REVIEW_CONTENT")
    @Size(min = 1, max = 500, message = "TOO_LONG_REVIEW_CONTENT")
    private String content;

    @NotNull(message = "EMPTY_REVIEW_FASTED")
    private boolean fasted;

    public ReviewRequest(Review review) {
        this.date = review.getDate();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.fasted = review.isFasted();
    }

    public Review toEntity(Food food){
        return Review.builder()
                .food(food)
                .date(date)
                .title(title)
                .content(content)
                .fasted(fasted)
                .build();
    }
}
