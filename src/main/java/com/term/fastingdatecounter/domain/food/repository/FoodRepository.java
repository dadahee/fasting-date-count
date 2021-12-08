package com.term.fastingdatecounter.domain.food.repository;

import com.term.fastingdatecounter.domain.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    List<Food> findByUserId(Long userId);
}
