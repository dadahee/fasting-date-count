package com.term.fastingdatecounter.api.food.controller.dto;

import com.term.fastingdatecounter.api.food.domain.Food;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FoodListResponse {

    private final List<FoodResponse> foodList;

    public FoodListResponse(List<Food> foodList){
        this.foodList = foodList.stream()
                .map(FoodResponse::new)
                .collect(Collectors.toList());
    }
}
