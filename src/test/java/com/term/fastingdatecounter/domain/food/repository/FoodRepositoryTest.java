package com.term.fastingdatecounter.domain.food.repository;

import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.user.domain.User;
import com.term.fastingdatecounter.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class FoodRepositoryTest {

    @Autowired
    FoodRepository foodRepository;

    @Autowired
    UserRepository userRepository;

    private User user;

    @AfterEach
    public void cleanAll() {
        foodRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name("test")
                .email("test@test.com")
                .build();
        userRepository.save(user);
    }

    public Food createFood() {
        return Food.builder()
                .user(user)
                .name("음식")
                .startDate(LocalDate.now())
                .build();
    }

    @Test
    @DisplayName("음식 저장, 생성일시 - 성공")
    public void save() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Food food = createFood();

        // when
        foodRepository.save(food);

        // then
        List<Food> foodList = foodRepository.findAll();
        assertThat(foodList).isNotEmpty();

        Food result = foodList.get(0);
        assertThat(food.getUser().getEmail()).isEqualTo(result.getUser().getEmail());
        assertThat(food.getName()).isEqualTo(result.getName());
        assertThat(food.getStartDate()).isEqualTo(result.getStartDate());
        assertThat(result.getCreatedAt()).isAfterOrEqualTo(now);
    }

    @Test
    @DisplayName("음식 수정 후 조회 시 변경된 Food 조회 - 성공")
    public void update() {
        // given
        Food food = createFood();
        Food saveResult = foodRepository.save(food);
        LocalDateTime beforeUpdateDateTime = saveResult.getUpdatedAt();

        // when
        String newName = "변경된 음식 이름";
        LocalDate newStartDate = LocalDate.MIN;
        saveResult.updateName(newName);
        saveResult.updateStartDate(newStartDate);

        // then
        //// 객체 존재 확인
        assertThat(foodRepository.findById(saveResult.getId())).isPresent();

        //// 변경 확인
        Food updatedResult = foodRepository.findById(saveResult.getId()).get();
        assertThat(updatedResult.getName()).isEqualTo(newName);
        assertThat(updatedResult.getStartDate()).isEqualTo(newStartDate);

        //// 수정일시 확인
        assertThat(updatedResult.getUpdatedAt()).isAfterOrEqualTo(beforeUpdateDateTime);
    }

    @Test
    @DisplayName("유저 아이디로 음식 목록 조회 - 성공")
    public void findByUserId() {
        // given
        Food food1 = createFood();
        Food food2 = createFood();
        Food food3 = createFood();
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