package com.term.fastingdatecounter.domain.review.service;

import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.food.repository.FoodRepository;
import com.term.fastingdatecounter.domain.food.service.FoodService;
import com.term.fastingdatecounter.domain.review.domain.Review;
import com.term.fastingdatecounter.domain.review.dto.ReviewRequest;
import com.term.fastingdatecounter.domain.review.repository.ReviewRepository;
import com.term.fastingdatecounter.domain.user.domain.User;
import com.term.fastingdatecounter.domain.user.repository.UserRepository;
import com.term.fastingdatecounter.global.exception.ServiceException;
import org.junit.jupiter.api.AfterEach;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@AutoConfigureMockMvc
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private FoodService foodService;

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    private User user;
    private Food food;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(123L)
                .name("test")
                .email("test@test.com")
                .build();
        food = Food.builder()
                .id(123L)
                .user(user)
                .name("test-food")
                .startDate(LocalDate.of(2021, 12, 1))
                .build();
    }

    @AfterEach
    void cleanAll() {
        foodRepository.deleteAll();
        userRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    private Review createReview(Long id, LocalDate date) {
        return Review.builder()
                .id(id)
                .food(food)
                .date(date)
                .title("review title")
                .content("review content")
                .fasted(true)
                .build();
    }

    private ReviewRequest createReviewRequest(LocalDate date) {
        return ReviewRequest.builder()
                .date(date)
                .title("review title")
                .content("review content")
                .fasted(true)
                .build();
    }

    private ReviewRequest createReviewRequest(String title, String content, LocalDate date, boolean fasted) {
        return ReviewRequest.builder()
                .date(date)
                .title(title)
                .content(content)
                .fasted(fasted)
                .build();
    }

    @Test
    @DisplayName("리뷰 목록 조회 - 성공")
    void findByFoodId() {
        // given
        //// 유저 가정
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 음식 가정
        given(foodRepository.findById(food.getId())).willReturn(Optional.of(food));

        //// 테스트 데이터
        Review review1 = createReview(1L, LocalDate.now().minusDays(3));
        Review review2 = createReview(2L, LocalDate.now().minusDays(2));
        Review review3 = createReview(3L, LocalDate.now().minusDays(1));
        List<Review> reviews = Arrays.asList(review3, review2, review1);


        //// 위의 리뷰 데이터 가정
        given(reviewRepository.findByFoodIdOrderByDateDesc(food.getId())).willReturn(reviews);

        // when
        List<Review> findResult = reviewService.findByFoodId(user.getId(), food.getId());

        // then
        assertThat(findResult).hasSize(3);
        assertThat(findResult).containsSequence(Arrays.asList(review3, review2, review1));
    }

    @Test
    @DisplayName("리뷰 목록 조회 - 실패(존재하지 않는 유저)")
    void findByFoodIdFailedWhenNotExistUser() {
        // given
        //// 유저 가정
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> reviewService.findByFoodId(user.getId(), food.getId()))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G04");
    }

    @Test
    @DisplayName("리뷰 목록 조회 - 실패(존재하지 않는 음식)")
    void findByFoodIdFailedWhenNotExistFood() {
        // given
        //// 유저 가정
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 음식 가정
        given(foodRepository.findById(food.getId())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> reviewService.findByFoodId(user.getId(), food.getId()))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("F03");
    }

    @Test
    @DisplayName("리뷰 목록 조회 - 실패(음식 작성자와 다른 유저)")
    void findByFoodIdFailedWhenUserWithoutAuthority() {
        // given
        //// 다른 유저
        User anotherUser = User.builder()
                .id(3333L)
                .name("foreigner")
                .email("foreigner@test.com")
                .build();

        //// 유저 가정
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.of(anotherUser));

        //// 음식 가정
        given(foodRepository.findById(food.getId())).willReturn(Optional.of(food));

        // when
        // then
        assertThatThrownBy(() -> reviewService.findByFoodId(anotherUser.getId(), food.getId()))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G02");
    }


    @Test
    @DisplayName("리뷰 등록 - 성공")
    void save() {
        // given
        //// 존재하는 유저 가정
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식 가정
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 테스트 리뷰 데이터
        ReviewRequest request = createReviewRequest(LocalDate.now());

        // when
        Review result = reviewService.save(user.getId(), food.getId(), request);

        // then
        assertAll(
                () -> assertThat(result.getFood().getId()).isEqualTo(food.getId()),
                () -> assertThat(result.getDate()).isEqualTo(request.getDate()),
                () -> assertThat(result.getTitle()).isEqualTo(request.getTitle()),
                () -> assertThat(result.getContent()).isEqualTo(request.getContent()),
                () -> assertThat(result.isFasted()).isEqualTo(request.isFasted())
        );
    }

    @Test
    @DisplayName("리뷰 등록 - 실패(존재하지 않는 유저)")
    void saveFailedWhenNotExistUser() {
        // given
        //// 존재하자 얺는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        //// 존재하는 음식 가정
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 테스트 리뷰 데이터
        ReviewRequest request = createReviewRequest(LocalDate.now());

        // when
        // then
        assertThatThrownBy(() -> reviewService.save(user.getId(), food.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G04");
    }

    @Test
    @DisplayName("리뷰 등록 - 실패(존재하지 않는 음식)")
    void saveFailedWhenNotExistFood() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하지 않는 음식 가정
        given(foodRepository.findById(user.getId())).willReturn(Optional.empty());

        //// 테스트 리뷰 데이터
        ReviewRequest request = createReviewRequest(LocalDate.now());

        // when
        // then
        assertThatThrownBy(() -> reviewService.save(user.getId(), food.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("F03");
    }

    @Test
    @DisplayName("리뷰 등록 - 실패(음식 작성자와 다른 유저의 요청)")
    void saveFailedWhenUserWithoutAuthority() {
        // given
        //// 다른 유저
        User anotherUser = User.builder()
                .id(3333L)
                .name("foreigner")
                .email("foreigner@test.com")
                .build();

        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.of(anotherUser));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 테스트 리뷰 데이터
        ReviewRequest request = createReviewRequest(LocalDate.now());

        // when
        // then
        assertThatThrownBy(() -> reviewService.save(anotherUser.getId(), food.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G02");
    }

    @Test
    @DisplayName("리뷰 등록 - 실패(리뷰 중복)")
    void saveFailedWhenDateConflicts() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        LocalDate today = LocalDate.now();

        //// 테스트 리뷰 데이터
        ReviewRequest request = createReviewRequest(LocalDate.now());

        //// 등록할 리뷰 날짜에 이미 작성된 리뷰
        Review writtenReview = createReview(1L, today);
        given(reviewRepository.findByFoodIdAndDate(food.getId(), today)).willReturn(Optional.of(writtenReview));

        // when
        // then
        assertThatThrownBy(() -> reviewService.save(user.getId(), food.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("R08");
    }

    @Test
    @DisplayName("리뷰 등록 - 실패(리뷰 날짜가 단식시작일보다 빠름)")
    void saveFailedWhenDateIsInvalid() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 테스트 리뷰 데이터
        ReviewRequest request = createReviewRequest(LocalDate.of(2020, 1, 1));

        // when
        // then
        assertThatThrownBy(() -> reviewService.save(user.getId(), food.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("R09");
    }

    @Test
    @DisplayName("리뷰 수정 - 성공")
    void update() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        //// 테스트 데이터
        ReviewRequest request = createReviewRequest("new title", "new content", LocalDate.now(), false);

        // when
        Review result = reviewService.update(user.getId(), food.getId(), review.getId(), request);

        // then
        assertAll(
                () -> assertThat(review.getId()).isEqualTo(result.getId()),
                () -> assertThat(request.getDate()).isEqualTo(result.getDate()),
                () -> assertThat(request.getTitle()).isEqualTo(result.getTitle()),
                () -> assertThat(request.getContent()).isEqualTo(result.getContent()),
                () -> assertThat(request.isFasted()).isEqualTo(result.isFasted())
        );
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(존재하지 않는 유저)")
    void updateFailedWhenNotExistUser() {
        // given
        //// 존재하지 않는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        //// 테스트 데이터
        ReviewRequest request = createReviewRequest("new title", "new content", LocalDate.now(), false);

        // when
        // then
        assertThatThrownBy(() -> reviewService.update(user.getId(), food.getId(), review.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G04");
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(존재하지 않는 음식)")
    void updateFailedWhenNotExistFood() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.empty());

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        //// 테스트 데이터
        ReviewRequest request = createReviewRequest("new title", "new content", LocalDate.now(), false);

        // when
        // then
        assertThatThrownBy(() -> reviewService.update(user.getId(), food.getId(), review.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("F03");
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(존재하지 않는 리뷰)")
    void updateFailedWhenNotExistReview() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.empty());

        //// 테스트 데이터
        ReviewRequest request = createReviewRequest("new title", "new content", LocalDate.now(), false);

        // when
        // then
        assertThatThrownBy(() -> reviewService.update(user.getId(), food.getId(), review.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("R07");
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(작성자와 다른 유저의 요청)")
    void updateFailedWhenUserWithoutAuthority() {
        // given
        //// 다른 유저
        User anotherUser = User.builder()
                .id(444L)
                .name("foreigner")
                .email("foreigner@test.com")
                .build();

        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.of(anotherUser));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        //// 테스트 데이터
        ReviewRequest request = createReviewRequest("new title", "new content", LocalDate.now(), false);

        // when
        // then
        assertThatThrownBy(() -> reviewService.update(anotherUser.getId(), food.getId(), review.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G02");
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(리뷰 중복)")
    void updateFailedWhenDateConflicts() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        //// 테스트 데이터
        LocalDate newDate = LocalDate.of(2021, 12, 3);
        ReviewRequest request = createReviewRequest("new title", "new content", newDate, false);

        //// 해당 날짜에 이미 작성된 리뷰
        Review writtenReview = createReview(11L, newDate);
        given(reviewRepository.findByFoodIdAndDate(food.getId(), newDate)).willReturn(Optional.of(writtenReview));

        // when
        // then
        assertThatThrownBy(() -> reviewService.update(user.getId(), food.getId(), review.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("R08");
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(리뷰 날짜가 단식시작일보다 빠름)")
    void updateFailedWhenDateIsInvalid() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        //// 테스트 데이터
        ReviewRequest request = createReviewRequest("new title", "new content", LocalDate.MIN, false);

        // when
        // then
        assertThatThrownBy(() -> reviewService.update(user.getId(), food.getId(), review.getId(), request))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("R09");
    }

    @Test
    @DisplayName("리뷰 삭제 - 성공")
    void delete() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        // when
        reviewService.delete(user.getId(), food.getId(), review.getId());

        // then
        then(reviewRepository)
                .should(times(1))
                .findById(review.getId());
        then(reviewRepository)
                .should(times(1))
                .delete(any(Review.class));
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패(존재하지 않는 유저)")
    void deleteFailedWhenNotExistUser() {
        // given
        //// 존재하지 않는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        // when
        // then
        assertThatThrownBy(() -> reviewService.delete(user.getId(), food.getId(), review.getId()))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G04");
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패(존재하지 않는 음식)")
    void deleteFailedWhenNotExistFood() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.empty());

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        // when
        // then
        assertThatThrownBy(() -> reviewService.delete(user.getId(), food.getId(), review.getId()))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("F03");
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패(존재하지 않는 리뷰)")
    void deleteFailedWhenNotExistReview() {
        // given
        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> reviewService.delete(user.getId(), food.getId(), review.getId()))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("R07");
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패(작성자와 다른 유저의 요청)")
    void deleteFailedWhenUserWithoutAuthority() {
        // given
        //// 다른 유저
        User anotherUser = User.builder()
                .id(3333L)
                .name("foreigner")
                .email("foreigner@test.com")
                .build();

        //// 존재하는 유저
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.of(anotherUser));

        //// 존재하는 음식
        given(foodRepository.findById(user.getId())).willReturn(Optional.of(food));

        //// 존재하는 리뷰
        Review review = createReview(10L, LocalDate.now());
        given(reviewRepository.findById(review.getId())).willReturn(Optional.of(review));

        // when
        // then
        assertThatThrownBy(() -> reviewService.delete(anotherUser.getId(), food.getId(), review.getId()))
                .isInstanceOf(ServiceException.class)
                .extracting(e -> ((ServiceException) e).getCode())
                .isEqualTo("G02");
    }
}