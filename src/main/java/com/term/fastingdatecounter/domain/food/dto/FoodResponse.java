package com.term.fastingdatecounter.domain.food.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.term.fastingdatecounter.domain.food.domain.Food;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class FoodResponse {

    private final Long id;
    private final String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    private final Long dayCount;
    private final Long dDay;

    public FoodResponse(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.startDate = food.getStartDate();
        this.dayCount = food.getDayCount();
        this.dDay = ChronoUnit.DAYS.between(startDate, LocalDate.now()) + 1L;
    }
}
