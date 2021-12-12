package com.term.fastingdatecounter.domain.food.service;

import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.food.dto.FoodRequest;
import com.term.fastingdatecounter.domain.food.repository.FoodRepository;
import com.term.fastingdatecounter.domain.review.domain.Review;
import com.term.fastingdatecounter.domain.review.dto.ReviewRequest;
import com.term.fastingdatecounter.domain.review.repository.ReviewRepository;
import com.term.fastingdatecounter.domain.review.service.ReviewService;
import com.term.fastingdatecounter.domain.user.domain.User;
import com.term.fastingdatecounter.domain.user.repository.UserRepository;
import com.term.fastingdatecounter.global.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@AutoConfigureMockMvc
class FoodServiceTest {

    @InjectMocks
    private FoodService foodService;

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(123L)
                .name("test")
                .email("test@test.com")
                .build();
    }

    @DisplayName("음식 목록 조회 - 성공")
    @Test
    void findByUserId() {
        // given
        //// 유저 존재 가정
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 테스트 데이터
        Food food1 = createFood(user, 1L);
        Food food2 = createFood(user, 1L);
        Food food3 = createFood(user, 1L);
        List<Food> foodList = new ArrayList<>(Arrays.asList(food1, food2, food3));

        //// 위의 등록 가정
        given(foodRepository.findByUserId(user.getId())).willReturn(foodList);

        // when : 음식 목록 조회
        List<Food> foundFoodList = foodService.findByUserId(user.getId());

        // then
        assertThat(foundFoodList).hasSize(3);
    }

    @DisplayName("음식 목록 조회 - 실패(존재하지 않는 유저)")
    @Test
    void findByUserIdFailedWhenNotExistuser() {
        // given
        //// 유저 찾을 수 없음
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        // when
        // then
        //// 발생한 에러가 G04(NOT_FOUND_USER)와 매치
        assertThatThrownBy(() -> foodService.findByUserId(user.getId()))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G04");
    }

    @DisplayName("음식 조회 - 성공")
    @Test
    void findById() {
        // given
        //// 테스트 음식 데이터
        Long foodId = 111L;
        Food food = createFood(user, foodId);
        given(foodRepository.findById(foodId)).willReturn(Optional.of(food));

        // when
        Food foundResult = foodService.findById(foodId);

        // then
        assertThat(foundResult).isNotNull();
        assertAll(
                () -> assertThat(food.getStartDate()).isEqualTo(foundResult.getStartDate()),
                () -> assertThat(food.getName()).isEqualTo(foundResult.getName()),
                () -> assertThat(food.getUser().getId()).isEqualTo(foundResult.getUser().getId())
        );
    }

    @DisplayName("음식 조회 - 실패(존재하지 않는 음식)")
    @Test
    void findByIdFailedWhenNotExistInterview() {
        // given
        //// 존재하지 않는 음식
        given(foodRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        //// 발생한 에러가 F03(NOT_FOUND_FOOD)와 매치
        assertThatThrownBy(() -> foodService.findById(1234L))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("F03");
    }

    @DisplayName("음식 등록 - 성공")
    @Test
    void save() {
        // given
        //// 테스트 음식 데이터
        FoodRequest foodRequest = createFoodRequest();

        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        // when
        Food result = foodService.save(user.getId(), foodRequest);

        // then
        assertAll(
                () -> assertThat(result.getUser().getId()).isEqualTo(user.getId()),
                () -> assertThat(result.getName()).isEqualTo(foodRequest.getName()),
                () -> assertThat(result.getStartDate()).isEqualTo(foodRequest.getStartDate())
        );
    }

    @DisplayName("음식 등록 - 실패(존재하지 않는 유저)")
    @Test
    void saveFailedWhenNotExistUser() {
        // given
        //// 존재하지 않는 유저 가정
        given(userRepository.findById(user.getId()))
                .willReturn(Optional.empty());
        FoodRequest foodRequest = createFoodRequest();

        // when
        // then
        //// 발생한 에러가 G04(NOT_FOUND_USER)와 매치
        assertThatThrownBy(() -> foodService.save(user.getId(), foodRequest))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G04");
    }

    @DisplayName("음식 수정 - 성공")
    @Test
    void update() {
        // given
        //// 테스트 음식 데이터
        Long foodId = 1L;
        FoodRequest foodRequest = createFoodRequest();
        Food food = createFood(user, foodId);

        //// 존재하는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(anyLong())).willReturn(Optional.of(food));

        // when
        Food result = foodService.update(user.getId(), foodId, foodRequest);

        // then
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(food.getName()).isEqualTo(result.getName()),
                () -> assertThat(food.getStartDate()).isEqualTo(result.getStartDate()),
                () -> assertThat(food.getUser().getId()).isEqualTo(result.getUser().getId())
        );
    }

    @DisplayName("음식 수정 - 실패(존재하지 않는 유저)")
    @Test
    void updateFailedWhenNotExistUser() {
        //// 테스트 음식 데이터
        Long foodId = 1L;
        FoodRequest foodRequest = createFoodRequest();
        Food food = createFood(user, foodId);

        //// 존재하지 않는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        //// 존재하는 음식
        given(foodRepository.findById(anyLong())).willReturn(Optional.of(food));

        // when
        // then
        //// 발생한 에러가 G04(NOT_FOUND_USER)와 매치
        assertThatThrownBy(() -> foodService.update(user.getId(), foodId, foodRequest))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G04");
    }

    @DisplayName("음식 수정 - 실패(존재하지 않는 음식)")
    @Test
    void updateFailedWhenNotExistFood() {
        //// 테스트 음식 데이터
        Long foodId = 1L;
        FoodRequest foodRequest = createFoodRequest();
        Food food = createFood(user, foodId);

        //// 존재하는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하지 않는 음식
        given(foodRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        //// 발생한 에러가 F03(NOT_FOUND_FOOD)와 매치
        assertThatThrownBy(() -> foodService.update(user.getId(), foodId, foodRequest))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("F03");
    }

    @DisplayName("음식 수정 - 실패(작성자가 아닌 유저)")
    @Test
    void updateFailedWhenUserWithoutAuthority() {
        // given
        //// 테스트 음식 데이터
        Long foodId = 1L;
        FoodRequest foodRequest = createFoodRequest();
        Food food = createFood(user, foodId);
        User anotherUser = User.builder()
                .id(12321L)
                .name("foreigner")
                .email("foreigner@test.com")
                .build();

        //// 존재하는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.of(anotherUser));

        //// 존재하는 음식 (원작성자: user)
        given(foodRepository.findById(anyLong())).willReturn(Optional.of(food));

        // when
        // then
        //// 발생한 에러가 G02(ACCESS_DENIED)와 매치
        assertThatThrownBy(() -> foodService.update(anotherUser.getId(), foodId, foodRequest))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G02");
    }

    @DisplayName("음식 수정 - 실패(단식시작일이 리뷰날짜보다 늦음)")
    @Test
    void updateFailedWhenStartDateIsInvalid() {
        // given
        //// 테스트 음식 데이터
        Long foodId = 1L;
        FoodRequest foodRequest = createFoodRequest();
        Food food = createFood(user, foodId);

        //// 테스트 리뷰 데이터
        Long reviewId = 3L;
        Review review = createReview(reviewId, food, food.getStartDate().minusDays(3));

        //// 존재하는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(food.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        given(reviewRepository.findByFoodIdOrderByDateDesc(foodId)).willReturn(Arrays.asList(review));
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        // then
        //// 발생한 에러가 F05(TOO_LATE_FOOD_START_DATE)와 매치
        assertThatThrownBy(() -> foodService.update(user.getId(), foodId, foodRequest))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("F05");

    }

    @DisplayName("음식 삭제 - 성공")
    @Test
    void delete() {
        // given
        //// 테스트 음식 데이터
        Long foodId = 1L;
        Food food = createFood(user, foodId);

        //// 존재하는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(food.getId())).willReturn(Optional.of(food));

        // when
        foodService.delete(user.getId(), foodId);

        // then
        then(foodRepository)
                .should(times(1))
                .findById(foodId);
        then(foodRepository)
                .should(times(1))
                .delete(any(Food.class));
    }

    @DisplayName("음식 삭제 - 실패(존재하지 않는 유저)")
    @Test
    void deleteFailedWhenNotExistUser() {
        // given
        //// 테스트 음식 데이터
        Long foodId = 1L;
        Food food = createFood(user, foodId);

        //// 존재하지 않는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        //// 존재하는 음식
        given(foodRepository.findById(food.getId())).willReturn(Optional.of(food));

        // when
        // then
        assertThatThrownBy(() -> foodService.delete(user.getId(), foodId))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G04");
    }

    @DisplayName("음식 삭제 - 실패(존재하지 않는 음식)")
    @Test
    void deleteFailedWhenNotExistFood() {
        // given
        //// 테스트 음식 데이터
        Long foodId = 1L;
        Food food = createFood(user, foodId);

        //// 존재하는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(food.getId())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> foodService.delete(user.getId(), foodId))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("F03");
    }

    @DisplayName("음식 삭제 - 실패(작성자가 아닌 유저)")
    @Test
    void deleteFailedWhenUserWithoutAuthority() {
        // given
        //// 테스트 음식 데이터
        Long foodId = 1L;
        Food food = createFood(user, foodId);

        //// 테스트 유저(작성자가 아닌 유저) 데이터
        User anotherUser = User.builder()
                .id(12321L)
                .name("foreigner")
                .email("foreigner@test.com")
                .build();

        //// 존재하는 사용자
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.of(anotherUser));

        //// 존재하는 음식
        given(foodRepository.findById(food.getId())).willReturn(Optional.of(food));

        // when
        // then
        assertThatThrownBy(() -> foodService.delete(anotherUser.getId(), foodId))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G02");
    }


    private Review createReview(Long id, Food food, LocalDate date) {
        return Review.builder()
                .id(id)
                .food(food)
                .date(date)
                .title("review title")
                .content("review content")
                .build();
    }

    private Food createFood(User author, Long id) {
        return Food.builder()
                .id(id)
                .user(author)
                .name("food")
                .startDate(LocalDate.of(2021, 12, 1))
                .build();
    }

    private FoodRequest createFoodRequest() {
        return FoodRequest.builder()
                .name("food")
                .startDate(LocalDate.of(2021, 12, 1))
                .build();

    }

}