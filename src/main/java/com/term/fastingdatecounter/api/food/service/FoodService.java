package com.term.fastingdatecounter.api.food.service;

import com.term.fastingdatecounter.api.food.controller.dto.FoodRequest;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository;

    public List<Food> find(Long userId) {
        // implement later
        return foodRepository.findByUserId(userId);
    }

    public void save(FoodRequest foodRequest) {
    }

    public void update(Long foodId, FoodRequest foodRequest) {
    }

    public void delete(Long foodId) {
    }
}
