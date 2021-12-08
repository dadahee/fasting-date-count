package com.term.fastingdatecounter.domain.food.repository;

import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.user.domain.User;
import com.term.fastingdatecounter.global.config.WithMockOAuthUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class FoodRepositoryTest {

    @Autowired
    FoodRepository foodRepository;

    // 단위 테스트가 끝날 때마다 foodRepository 초기화
    @AfterEach
    public void cleanAll() {
        foodRepository.deleteAll();
    }


    public Food createFood() {
        User user = User.builder()
                .id(123L)
                .name("test")
                .email("test@test.com")
                .build();
        return Food.builder()
                .user(user)
                .name("음식")
                .startDate(LocalDate.now())
                .build();
    }

    public Food createFoodOfUser(User user) {
        return Food.builder()
                .user(user)
                .name("음식")
                .startDate(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("음식 저장, 생성일시 (성공)")
    public void save() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Food food = createFood();

        // when
        foodRepository.save(food);

        // then
        List<Food> foodList = foodRepository.findAll();
        Food result = foodList.get(0);
        assertThat(food.getUser()).isEqualTo(result.getUser());
        assertThat(food.getName()).isEqualTo(result.getName());
        assertThat(food.getStartDate()).isEqualTo(result.getStartDate());
        assertThat(result.getCreatedAt()).isAfter(now);
    }

    @Test
    @DisplayName("음식 수정 후 조회 시 변경된 Food 조회")
    public void update() {
        // given
        Food food = createFood();
        Food saveResult = foodRepository.save(food);
        LocalDateTime beforeUpdateDateTime = saveResult.getUpdatedAt();

        // when
        saveResult.updateName("변경된 음식 이름");
        saveResult.updateStartDate(LocalDate.MIN);

        // then
        //// 객체 존재 확인
        assertThat(foodRepository.findById(saveResult.getId())).isPresent();

        //// 변경 확인
        Food updatedResult = foodRepository.findById(saveResult.getId()).get();
        assertThat(updatedResult.getName()).isEqualTo("변경된 음식 이름");
        assertThat(updatedResult.getStartDate()).isEqualTo(LocalDate.MIN);

        //// 수정일시 확인
        assertThat(updatedResult.getUpdatedAt()).isAfter(beforeUpdateDateTime);
    }

    @Test
    @DisplayName("유저 아이디로 음식 목록 조회")
    public void findByUserId() {
        // given
        User user = User.builder()
                .id(123L)
                .name("test")
                .email("test@test.com")
                .build();

        Food food1 = createFoodOfUser(user);
        Food food2 = createFoodOfUser(user);
        Food food3 = createFoodOfUser(user);
        Food saveResult1 = foodRepository.save(food1);
        Food saveResult2 = foodRepository.save(food2);
        Food saveResult3 = foodRepository.save(food3);

        // when
        List<Long> foodIdList = foodRepository.findByUserId(user.getId())
                .stream().map(f -> f.getId()).collect(Collectors.toList());

        // then
        //// 크기 확인
        assertThat(foodIdList).hasSize(3);

        //// 요소 확인
        assertThat(foodIdList).contains(saveResult1.getId());
        assertThat(foodIdList).contains(saveResult2.getId());
        assertThat(foodIdList).contains(saveResult3.getId());
    }

    @Test
    @DisplayName("음식 아이디로 조회")
    public void findById() {
        // given
        Food food = createFood();
        Food saveResult = foodRepository.save(food);

        // when
        Optional<Food> findResult = foodRepository.findById(saveResult.getId());

        // then
        assertThat(findResult).isPresent();
        assertThat(findResult).containsSame(saveResult);
    }
}