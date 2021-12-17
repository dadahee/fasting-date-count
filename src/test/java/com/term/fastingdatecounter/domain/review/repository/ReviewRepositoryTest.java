package com.term.fastingdatecounter.domain.review.repository;

import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.food.repository.FoodRepository;
import com.term.fastingdatecounter.domain.review.domain.Review;
import com.term.fastingdatecounter.domain.user.domain.User;
import com.term.fastingdatecounter.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FoodRepository foodRepository;

    private User user;
    private Food food;

    @AfterEach
    public void cleanAll() {
        reviewRepository.deleteAll();
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .name("test")
                .email("test@test.com")
                .build();
        food = Food.builder()
                .user(user)
                .name("food")
                .startDate(LocalDate.of(2021, 12, 1))
                .build();
        userRepository.save(user);
        foodRepository.save(food);
    }

    private Review createReview() {
        return Review.builder()
                .food(food)
                .title("review title")
                .content("review content")
                .date(LocalDate.now())
                .fasted(true)
                .build();
    }

    private Review createReview(LocalDate date, boolean fasted) {
        return Review.builder()
                .food(food)
                .title("review title")
                .content("review content")
                .date(date)
                .fasted(fasted)
                .build();
    }

    @Test
    @DisplayName("리뷰 등록")
    void save() {
        // given
        Review review = createReview();
        LocalDateTime now = LocalDateTime.now();

        // when
        reviewRepository.save(review);

        // then
        //// 등록 확인
        List<Review> reviews = reviewRepository.findAll();
        assertThat(reviews).isNotEmpty();

        Review result = reviews.get(0);
        //// 등록 정보 확인
        assertAll(
                () -> assertThat(result.getFood().getId()).isEqualTo(review.getFood().getId()),
                () -> assertThat(result.getTitle()).isEqualTo(review.getTitle()),
                () -> assertThat(result.getContent()).isEqualTo(review.getContent()),
                () -> assertThat(result.getDate()).isEqualTo(review.getDate()),
                () -> assertThat(result.isFasted()).isEqualTo(review.isFasted())
        );
        //// 생성일시 확인
        assertThat(result.getCreatedAt()).isAfterOrEqualTo(now);
    }

    @Test
    @DisplayName("리뷰 수정")
    void update() {
        // given
        //// 리뷰 수정을 위한 테스트 리뷰 등록
        Review review = createReview();
        Review saveResult = reviewRepository.save(review);
        LocalDateTime beforeUpdateDateTime = saveResult.getUpdatedAt();

        //// 테스트 데이터
        LocalDate newStartDate = LocalDate.now();
        String newTitle = "review title updated";
        String newContent = "review contnet updated";
        boolean newFasted = false;

        // when
        saveResult.updateReview(newStartDate, newTitle, newContent, newFasted);

        // then
        //// 객체 존재 확인
        assertThat(reviewRepository.findById(saveResult.getId())).isPresent();
        Review updateResult = reviewRepository.findById(saveResult.getId()).get();

        //// 기본 정보 확인
        assertThat(updateResult.getId()).isEqualTo(saveResult.getId()); // pk 수정 전후 일치 확인
        assertThat(updateResult.getFood().getId()).isEqualTo(saveResult.getFood().getId()); // 음식 id 일치 확인

        //// 변경 확인
        assertAll(
                () -> assertThat(updateResult.getDate()).isEqualTo(newStartDate),
                () -> assertThat(updateResult.getTitle()).isEqualTo(newTitle),
                () -> assertThat(updateResult.getContent()).isEqualTo(newContent),
                () -> assertThat(updateResult.isFasted()).isEqualTo(newFasted)
        );

        //// 수정일시 확인
        assertThat(updateResult.getUpdatedAt()).isAfterOrEqualTo(beforeUpdateDateTime);
    }

    @Test
    @DisplayName("음식 아이디로 리뷰 목록 조회 (날짜 내림차순)")
    void findByFoodIdOrderByDateDesc() {
        // given
        //// 테스트 리뷰 등록
        for (int i=1; i<6; i++){
            Review review = createReview(LocalDate.of(2021, 12, i), true);
            reviewRepository.save(review);
        }

        // when
        List<Review> reviews = reviewRepository.findByFoodIdOrderByDateDesc(food.getId());

        // then
        //// 리뷰 개수 확인
        assertThat(reviews).hasSize(5);

        //// 리뷰 날짜 및 날짜 내림차순 확인
        for (int i=0; i<4; i++) {
            // 하드코딩한 샘플 날짜와의 일치 여부 확인
            assertThat(reviews.get(i).getDate()).isEqualTo(LocalDate.of(2021, 12, 5 - i));

            // 다음 요소보다 최신 날짜인지 확인
            assertThat(reviews.get(i).getDate()).isAfter(reviews.get(i + 1).getDate());

        }
    }

    @Test
    @DisplayName("음식 아이디 & 날짜로 리뷰 단건 조회")
    void findByFoodIdAndDate() {
        // given
        //// 테스트 리뷰 등록
        Review review = createReview();
        Review saveResult = reviewRepository.save(review);

        // when
        Optional<Review> findResult = reviewRepository.findByFoodIdAndDate(food.getId(), saveResult.getDate());

        // then
        assertThat(findResult).contains(saveResult);
    }

    @Test
    @DisplayName("단식 일수 카운트 - 음식 아이디와 단식 여부(fasted)로 조회")
    void countByFoodIdAndFastedIsTrue() {
        // given
        for (int i=1; i<6; i++) {
            Review review = createReview(LocalDate.of(2021, 12, i), i % 2 == 0);
            reviewRepository.save(review);
        }

        // when
        Long count = reviewRepository.countByFoodIdAndFastedIsTrue(food.getId());

        // then
        assertThat(count).isEqualTo(2);
    }
}