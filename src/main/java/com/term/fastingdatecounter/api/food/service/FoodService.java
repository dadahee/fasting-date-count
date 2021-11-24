package com.term.fastingdatecounter.api.food.service;

import com.term.fastingdatecounter.api.food.controller.dto.FoodRequest;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
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

    @Transactional(readOnly = true)
    public Food findById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ServiceException("존재하지 않는 음식입니다."));
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
