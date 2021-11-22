package com.term.fastingdatecounter.api.food.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.user.domain.User;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
public class FoodRequest {

    @NotNull
    private User user;

    @NotNull
    @Size(max = 50)
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    public FoodRequest(User user, Food food) {
        this.user = user;
        this.name = food.getName();
        this.startDate = food.getStartDate();
    }

    public Food toEntity() {
        return Food.builder() // 유저 처리 필요
                .user(user)
                .name(name)
                .startDate(startDate)
                .build();
    }

}
