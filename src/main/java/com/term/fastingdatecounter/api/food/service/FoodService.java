package com.term.fastingdatecounter.api.food.service;

import com.term.fastingdatecounter.api.food.controller.dto.FoodRequest;
import com.term.fastingdatecounter.api.food.domain.Food;
import com.term.fastingdatecounter.api.food.repository.FoodRepository;
import com.term.fastingdatecounter.api.user.domain.User;
import com.term.fastingdatecounter.api.user.repository.UserRepository;
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
    private final UserRepository userRepository;

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
    public Food save(Long userId, FoodRequest foodRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("계정을 찾을 수 없습니다."));
        Food food = foodRequest.toEntity(user);
        foodRepository.save(food);
        return food;
    }

    @Transactional
    public Food update(Long userId, Long foodId, FoodRequest foodRequest) {
        User user = findUserById(userId);
        Food food = findFoodById(foodId);
        food.update(foodRequest.getName(), foodRequest.getStartDate());
        return food;
    }

    @Transactional
    public void delete(Long userId, Long foodId) {
        User user = findUserById(userId);
        Food food = findFoodById(foodId);
        foodRepository.delete(food);
    }

    public User findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ServiceException("계정을 찾을 수 없습니다."));
        return user;
    }

    public Food findFoodById(Long foodId) {
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ServiceException("음식을 찾을 수 없습니다."));
        return food;
    }
}
