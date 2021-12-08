package com.term.fastingdatecounter.domain.food.dto;

import com.term.fastingdatecounter.domain.food.domain.Food;
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
