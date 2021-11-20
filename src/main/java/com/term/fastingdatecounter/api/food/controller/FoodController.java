package com.term.fastingdatecounter.api.food.controller;

import com.term.fastingdatecounter.api.food.controller.dto.FoodListResponse;
import com.term.fastingdatecounter.api.food.controller.dto.FoodRequest;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/food")
@RestController
public class FoodController {

    private final FoodService foodService;

    @GetMapping
    public ResponseEntity<FoodListResponse> find(){
        // get user Id
        Long userId = Long.valueOf(1); // implement later
        List<Food> foodList = foodService.find(userId); // implement later
        return ResponseEntity.ok(new FoodListResponse(foodList));
    }

    @PostMapping
    public ResponseEntity<Void> save(
            @RequestBody FoodRequest foodRequest
    ){
        foodService.save(foodRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{foodId}")
    public ResponseEntity<Void> update(
            @PathVariable(name = "foodId") Long foodId,
            @RequestBody FoodRequest foodRequest
    ){
        foodService.update(foodId, foodRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{foodId}")
    public ResponseEntity<Void> update(
            @PathVariable(name = "foodId") Long foodId
    ){
        foodService.delete(foodId);
        return ResponseEntity.noContent().build();
    }
}
