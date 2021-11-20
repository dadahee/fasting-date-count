package com.term.fastingdatecounter.api.food.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.api.food.domain.Food;
import lombok.Getter;

import java.util.Date;

@Getter
public class FoodResponse {

    private final Long id;
    private final String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final Date startDate;

    private final int dayCount;

    public FoodResponse(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.startDate = food.getStartDate();
        this.dayCount = food.getDayCount();
    }
}
