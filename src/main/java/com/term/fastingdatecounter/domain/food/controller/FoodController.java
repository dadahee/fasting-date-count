package com.term.fastingdatecounter.domain.food.controller;

import com.term.fastingdatecounter.domain.food.dto.FoodResponse;
import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.food.service.FoodService;
import com.term.fastingdatecounter.domain.user.dto.SessionUser;
import com.term.fastingdatecounter.domain.user.domain.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

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
        List<FoodResponse> foodListResponse = foodList.stream()
                .map(FoodResponse::new)
                .collect(Collectors.toList());
        model.addAttribute("user", user);
        model.addAttribute("foodList", foodListResponse);
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
