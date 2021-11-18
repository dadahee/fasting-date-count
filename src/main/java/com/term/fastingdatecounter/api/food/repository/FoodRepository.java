package com.term.fastingdatecounter.api.food.repository;

import com.term.fastingdatecounter.api.food.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
}
