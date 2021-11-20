package com.term.fastingdatecounter.api.food.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.api.food.domain.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
public class FoodRequest {

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    public FoodRequest(Food food) {
        this.name = food.getName();
        this.startDate = food.getStartDate();
    }

    public Food toEntity() {
        return Food.builder() // 유저 처리 필요
                .name(name)
                .startDate(startDate)
                .build();
    }
}
