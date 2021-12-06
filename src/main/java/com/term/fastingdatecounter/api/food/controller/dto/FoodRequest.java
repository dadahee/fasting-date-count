package com.term.fastingdatecounter.api.food.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class FoodRequest {

    @NotBlank(message = "EMPTY_FOOD_NAME")
    @Size(min = 1, max = 50, message = "TOO_LONG_FOOD_NAME")
    private String name;

    @PastOrPresent(message = "FUTURE_FOOD_START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    public FoodRequest(Food food) {
        this.name = food.getName();
        this.startDate = food.getStartDate();
    }

    public Food toEntity(User user) {
        return Food.builder() // 유저 처리 필요
                .user(user)
                .name(name)
                .startDate(startDate)
                .build();
    }
}
