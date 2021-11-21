package com.term.fastingdatecounter.api.food.controller;

import com.term.fastingdatecounter.api.food.controller.dto.FoodListResponse;
import com.term.fastingdatecounter.api.food.controller.dto.FoodRequest;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "음식(Food) API")
@RequiredArgsConstructor
@RequestMapping("/api/food")
@RestController
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "음식 목록 조회")
    @GetMapping
    public ResponseEntity<FoodListResponse> find(){
        // get user Id
        Long userId = Long.valueOf(1); // implement later
        List<Food> foodList = foodService.find(userId); // implement later
        return ResponseEntity.ok(new FoodListResponse(foodList));
    }

    @Operation(summary = "음식 등록")
    @PostMapping
    public ResponseEntity<Void> save(
            @RequestBody FoodRequest foodRequest
    ){
        foodService.save(foodRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "음식 수정")
    @PutMapping("/{foodId}")
    public ResponseEntity<Void> update(
            @PathVariable(name = "foodId") Long foodId,
            @RequestBody FoodRequest foodRequest
    ){
        foodService.update(foodId, foodRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "음식 삭제")
    @DeleteMapping("/{foodId}")
    public ResponseEntity<Void> update(
            @PathVariable(name = "foodId") Long foodId
    ){
        foodService.delete(foodId);
        return ResponseEntity.noContent().build();
    }
}
