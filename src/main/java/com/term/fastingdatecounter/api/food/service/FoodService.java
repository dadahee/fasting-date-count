package com.term.fastingdatecounter.api.food.service;

import com.term.fastingdatecounter.api.food.controller.dto.FoodRequest;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodService {

    private final FoodRepository foodRepository;

    @Transactional(readOnly = true)
    public List<Food> findByUserId(Long userId) {
        return foodRepository.findByUserId(userId);
    }

    @Transactional
    public void save(Long userId, FoodRequest foodRequest) {
    }

    @Transactional
    public void update(Long userId, Long foodId, FoodRequest foodRequest) {
    }

    @Transactional
    public void delete(Long userId, Long foodId) {
    }
}
