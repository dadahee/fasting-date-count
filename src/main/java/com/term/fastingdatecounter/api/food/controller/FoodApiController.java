package com.term.fastingdatecounter.api.food.controller;

import com.term.fastingdatecounter.api.food.controller.dto.FoodListResponse;
import com.term.fastingdatecounter.api.food.controller.dto.FoodRequest;
import com.term.fastingdatecounter.api.food.controller.dto.FoodResponse;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.service.FoodService;
import com.term.fastingdatecounter.api.user.controller.dto.dto.SessionUser;
import com.term.fastingdatecounter.api.user.domain.LoginUser;
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
public class FoodApiController {

    private final FoodService foodService;

    @Operation(summary = "음식 조회")
    @GetMapping
    public ResponseEntity<FoodListResponse> find(
            @LoginUser SessionUser user
    ){
        List<Food> foodList = foodService.findByUserId(user.getId()); // find food list by session user id
        return ResponseEntity.ok(new FoodListResponse(foodList));
    }


    @Operation(summary = "음식 등록")
    @PostMapping
    public ResponseEntity<FoodResponse> save(
            @LoginUser SessionUser user,
            @RequestBody FoodRequest foodRequest
    ){
        Food food = foodService.save(user.getId(), foodRequest);
        return ResponseEntity.ok(new FoodResponse(food));
    }

    @Operation(summary = "음식 수정")
    @PutMapping("/{foodId}")
    public ResponseEntity<FoodResponse> update(
            @LoginUser SessionUser user,
            @PathVariable(name = "foodId") Long foodId,
            @RequestBody FoodRequest foodRequest
    ){
        Food food = foodService.update(user.getId(), foodId, foodRequest);
        return ResponseEntity.ok(new FoodResponse(food));
    }

    @Operation(summary = "음식 삭제")
    @DeleteMapping("/{foodId}")
    public ResponseEntity<Void> update(
            @LoginUser SessionUser user,
            @PathVariable(name = "foodId") Long foodId
    ){
        foodService.delete(user.getId(), foodId);
        return ResponseEntity.noContent().build();
    }
}
