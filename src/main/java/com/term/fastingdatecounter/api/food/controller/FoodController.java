package com.term.fastingdatecounter.api.food.controller;

import com.term.fastingdatecounter.api.food.controller.dto.FoodListResponse;
import com.term.fastingdatecounter.api.food.controller.dto.FoodResponse;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.service.FoodService;
import com.term.fastingdatecounter.api.user.controller.dto.dto.SessionUser;
import com.term.fastingdatecounter.api.user.domain.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "음식 관련 페이지")
@RequiredArgsConstructor
@RequestMapping("/food")
@Controller
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "음식 목록 페이지")
    @GetMapping
    public String food(Model model, @LoginUser SessionUser user){
        List<Food> foodList = foodService.findByUserId(user.getId()); // find food list by session user id

        model.addAttribute("userEmail", user.getEmail());
        model.addAttribute("foodList", new FoodListResponse(foodList));
        return "food";
    }

    @Operation(summary = "음식 등록 페이지")
    @GetMapping("/save")
    public String foodSaveForm() {
        return "food-save";
    }

    @Operation(summary = "음식 수정 페이지")
    @GetMapping("/update/{foodId}")
    public String foodUpdateForm(Model model, @PathVariable Long foodId){
        Food food = foodService.findById(foodId);
        model.addAttribute("food", new FoodResponse(food));
        return "food-update";
    }
}
