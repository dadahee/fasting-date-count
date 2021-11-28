package com.term.fastingdatecounter.api.review.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.review.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@NoArgsConstructor
public class ReviewRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @NotBlank
    @Size(min = 1, max = 100)
    private String title;

    @NotNull
    @Size(min = 1, max = 500)
    private String content;

    @NotNull
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
