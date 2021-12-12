package com.term.fastingdatecounter.domain.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.food.repository.FoodRepository;
import com.term.fastingdatecounter.domain.review.domain.Review;
import com.term.fastingdatecounter.domain.review.dto.ReviewRequest;
import com.term.fastingdatecounter.domain.review.repository.ReviewRepository;
import com.term.fastingdatecounter.domain.user.domain.User;
import com.term.fastingdatecounter.domain.user.dto.SessionUser;
import com.term.fastingdatecounter.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
//@WebMvcTest(controllers = ReviewController.class)
@Transactional
@AutoConfigureMockMvc
class ReviewApiControllerTest {
    private String PREFIX_URI;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private User user;
    private Food food;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        user = userRepository.save(User.builder()
                .name("test-user")
                .email("test@test.com")
                .build());
        food = foodRepository.save(Food.builder()
                .user(user)
                .name("test-food")
                .startDate(LocalDate.of(2021, 12, 1))
                .build());
        session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(user));
        PREFIX_URI = "/api/food/" + food.getId() + "/reviews";
    }

    @AfterEach
    void cleanAll() {
        reviewRepository.deleteAll();
        foodRepository.deleteAll();
        userRepository.deleteAll();
    }

    private ReviewRequest createReviewRequest() {
        return ReviewRequest.builder()
                .date(LocalDate.now().minusDays(3))
                .title("review title")
                .content("review content")
                .fasted(true)
                .build();
    }

    @Test
    @DisplayName("리뷰 목록 조회 - 성공")
    void findReview() throws Exception {
        // given
        String url = PREFIX_URI;

        // when
        ResultActions result = mvc.perform(get(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        })));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewList").exists());
    }

    @Test
    @DisplayName("리뷰 목록 조회 - 실패(로그인 X)")
    void findReviewFailedWhenUserIsUnAuthenticated() throws Exception {
        // given
        String url = PREFIX_URI;

        // when
        ResultActions result = mvc.perform(get(url));

        // then
        result.andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("리뷰 목록 조회 - 실패(존재하지 않는 음식)")
    void findReviewFailedWhenNotExistFood() throws Exception {
        // given
        String url = "/api/food/123454/reviews";

        // when
        ResultActions result = mvc.perform(get(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        })));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("F03"));
    }

    @Test
    @DisplayName("리뷰 목록 조회 - 성공")
    void findReviewFailedWhenUserWithoutAuthority() throws Exception {
        // given
        String url = PREFIX_URI;

        // when
        ResultActions result = mvc.perform(get(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        })));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewList").exists());
    }

    @Test
    @DisplayName("리뷰 등록 - 성공")
    void saveReview() throws Exception {
        // given
        String url = PREFIX_URI;

        //// test review data
        ReviewRequest request = createReviewRequest();

        // when
        ResultActions result = mvc.perform(post(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(request)));

        // then
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.date")
                        .value(request.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.content").value(request.getContent()))
                .andExpect(jsonPath("$.fasted").value(request.isFasted()));
    }

    @Test
    @DisplayName("리뷰 등록 - 실패(로그인 X)")
    void saveReviewFailedWhenUserIsUnAuthenticated() throws Exception {
        // given
        String url = PREFIX_URI;

        //// test review data
        ReviewRequest request = createReviewRequest();

        // when
        ResultActions result = mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(request)));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/google"));
    }

    @Test
    @DisplayName("리뷰 등록 - 실패(음식 작성자와 불일치)")
    void saveReviewFailedWhenUserWithoutAuthority() throws Exception {
        // given
        String url = PREFIX_URI;

        //// another user
        user = userRepository.save(User.builder()
                .name("foreigner")
                .email("foreigner@test.com")
                .build());
        session.setAttribute("user", new SessionUser(user));


        //// test review data
        ReviewRequest request = createReviewRequest();

        // when
        ResultActions result = mvc.perform(post(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(request)));

        // then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("G02"));
    }


    @Test
    @WithMockUser
    @DisplayName("리뷰 수정 - 성공")
    void updateReview() throws Exception {
        // given
        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        //// make url
        String url = PREFIX_URI + "/" + saveReview.getId();

        //// test review data for testing update feature
        ReviewRequest updateRequest = ReviewRequest.builder()
                .date(LocalDate.now().minusDays(1))
                .title("review title updated")
                .content("review title updated")
                .fasted(false)
                .build();

        // when
        ResultActions result = mvc.perform(put(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saveReview.getId()))
                .andExpect(jsonPath("$.date")
                        .value(updateRequest.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.title").value(updateRequest.getTitle()))
                .andExpect(jsonPath("$.content").value(updateRequest.getContent()))
                .andExpect(jsonPath("$.fasted").value(updateRequest.isFasted()));
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(로그인 X)")
    void updateReviewFailedWhenUserIsUnAuthenticated() throws Exception {
        // given
        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        //// make url
        String url = PREFIX_URI + "/" + saveReview.getId();

        //// test review data for testing update feature
        ReviewRequest updateRequest = ReviewRequest.builder()
                .date(LocalDate.now().minusDays(1))
                .title("review title updated")
                .content("review title updated")
                .fasted(false)
                .build();

        // when
        ResultActions result = mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(작성자와 불일치)")
    void updateReviewFailedWhenUserWithoutAuthority() throws Exception {
        // given
        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        //// make url
        String url = PREFIX_URI + "/" + saveReview.getId();

        //// another user
        User user = userRepository.save(User.builder()
                .name("foreigner user")
                .email("foreigner@gmail.com")
                .build());
        session.setAttribute("user", new SessionUser(user));

        //// test review data for testing update feature
        ReviewRequest updateRequest = ReviewRequest.builder()
                .date(LocalDate.now().minusDays(1))
                .title("review title updated")
                .content("review title updated")
                .fasted(false)
                .build();

        // when
        ResultActions result = mvc.perform(put(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value("G02"));
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(존재하지 않는 리뷰)")
    void updateReviewFailedWhenNotExistReview() throws Exception {
        // given
        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        //// make url
        String url = PREFIX_URI + "/1234321";

        //// test review data for testing update feature
        ReviewRequest updateRequest = ReviewRequest.builder()
                .date(LocalDate.now().minusDays(1))
                .title("review title updated")
                .content("review title updated")
                .fasted(false)
                .build();

        // when
        ResultActions result = mvc.perform(put(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("R07"));
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(존재하지 않는 음식)")
    void updateReviewFailedWhenNotExistFood() throws Exception {
        // given
        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        //// make url
        String url = "/api/food/987654/reviews/" + saveReview.getId();

        //// test review data for testing update feature
        ReviewRequest updateRequest = ReviewRequest.builder()
                .date(LocalDate.now().minusDays(1))
                .title("review title updated")
                .content("review title updated")
                .fasted(false)
                .build();

        // when
        ResultActions result = mvc.perform(put(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("F03"));
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(날짜가 단식시작일보다 빠름)")
    void updateReviewFailedWhenDateIsInvalid() throws Exception {
        // given
        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        //// make url
        String url = PREFIX_URI + "/" + saveReview.getId();

        //// test review data for testing update feature
        ReviewRequest updateRequest = ReviewRequest.builder()
                .date(food.getStartDate().minusDays(3))
                .title("review title updated")
                .content("review title updated")
                .fasted(false)
                .build();

        // when
        ResultActions result = mvc.perform(put(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("R09"));
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(미래 날짜)")
    void updateReviewFailedWhenDateIsFuture() throws Exception {
        // given
        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        //// make url
        String url = PREFIX_URI + "/" + saveReview.getId();

        //// test review data for testing update feature
        ReviewRequest updateRequest = ReviewRequest.builder()
                .date(LocalDate.now().plusDays(10))
                .title("review title updated")
                .content("review title updated")
                .fasted(false)
                .build();

        // when
        ResultActions result = mvc.perform(put(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("R01"));
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(이미 존재)")
    void updateReviewFailedWhenDateConflicts() throws Exception {
        // given
        //// other review data
        ReviewRequest otherRequest = ReviewRequest.builder()
                .date(LocalDate.now())
                .title("other review title")
                .content("other review content")
                .fasted(false)
                .build();
        Review otherReview = reviewRepository.save(otherRequest.toEntity(food));

        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        //// make url
        String url = PREFIX_URI + "/" + saveReview.getId();

        //// test review data for testing update feature
        ReviewRequest updateRequest = ReviewRequest.builder()
                .date(LocalDate.now())
                .title("review title updated")
                .content("review title updated")
                .fasted(false)
                .build();

        // when
        ResultActions result = mvc.perform(put(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        }))
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .writeValueAsString(updateRequest)));

        // then
        result.andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("R08"));
    }

    @Test
    @DisplayName("리뷰 삭제")
    void deleteReview() throws Exception {
        // given
        //// test review data in advance
        ReviewRequest saveRequest = createReviewRequest();
        Review saveReview = reviewRepository.save(saveRequest.toEntity(food));

        System.out.println(saveReview.getId());

        //// make url
        String url = PREFIX_URI + "/" + saveReview.getId();

        // when
        ResultActions result = mvc.perform(delete(url)
                .session(session)
                .with(oauth2Login()
                        .attributes(attributes -> {
                            attributes.put("id", user.getId());
                            attributes.put("name", user.getName());
                            attributes.put("email", user.getEmail());
                        })));

        // then
        result.andExpect(status().isNoContent());
    }
}