package com.term.fastingdatecounter.api.food.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@Getter
public class FoodRequest {

    @NotBlank
    @Size(min = 1, max = 50)
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

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
