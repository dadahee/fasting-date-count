package com.term.fastingdatecounter.domain.food.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.user.domain.User;
import lombok.Builder;
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
    @Size(max = 50, message = "TOO_LONG_FOOD_NAME")
    private String name;

    @PastOrPresent(message = "FUTURE_FOOD_START_DATE") // not working
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Builder
    public FoodRequest(String name, LocalDate startDate) {
        this.name = name;
        this.startDate = startDate;
    }

    public Food toEntity(User user) {
        return Food.builder() // 유저 처리 필요
                .user(user)
                .name(name)
                .startDate(startDate)
                .build();
    }
}
