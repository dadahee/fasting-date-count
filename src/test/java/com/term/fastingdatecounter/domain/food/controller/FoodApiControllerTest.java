package com.term.fastingdatecounter.domain.food.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.term.fastingdatecounter.domain.food.domain.Food;
import com.term.fastingdatecounter.domain.food.dto.FoodRequest;
import com.term.fastingdatecounter.domain.food.repository.FoodRepository;
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

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
//@WebMvcTest(controllers = FoodController.class)
@Transactional
@AutoConfigureMockMvc
class FoodApiControllerTest {

    private final String PREFIX_URI = "/api/food";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    private MockHttpSession session;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodRepository foodRepository;

    private User user;

    @BeforeEach
    public void setup() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        user = userRepository.save(User.builder()
                .name("test-user")
                .email("test@test.com")
                .build());
        session = new MockHttpSession();
        session.setAttribute("user", new SessionUser(user));
    }

    @AfterEach
    void cleanAll() {
        foodRepository.deleteAll();
        userRepository.deleteAll();
    }

    private FoodRequest createFoodRequest() {
        return FoodRequest.builder()
                .name("food")
                .startDate(LocalDate.of(2021, 12, 1))
                .build();
    }

    private FoodRequest createFoodRequest(String name, LocalDate date) {
        return FoodRequest.builder()
                .name(name)
                .startDate(date)
                .build();
    }

    @Test
    @DisplayName("음식 목록 조회 - 성공")
    void find() throws Exception {
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
                .andExpect(jsonPath("$.foodList").exists());
    }

    @Test
    @DisplayName("음식 목록 조회 - 실패(로그인 X)")
    void findFailedWhenUserIsUnAuthenticated() throws Exception {
        // given
        String url = PREFIX_URI;

        // when
        ResultActions result = mvc.perform(get(url));

        // then
        result.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/oauth2/authorization/google"));
    }

    @Test
    @DisplayName("음식 등록 - 성공")
    void saveFood() throws Exception {
        // given
        String url = PREFIX_URI;

        //// test food data
        FoodRequest request = createFoodRequest();

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
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.startDate")
                        .value(request.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }

    @Test
    @DisplayName("음식 등록 - 실패(로그인 X)")
    void saveFoodFailedWhenUserIsUnAuthenticated() throws Exception {
        // given
        String url = PREFIX_URI;

        //// test food data
        FoodRequest request = createFoodRequest();

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
    @WithMockUser
    @DisplayName("음식 수정 - 성공")
    void updateFood() throws Exception {
        // given
        //// test food data in advance
        FoodRequest saveRequest = createFoodRequest();
        Food saveFood = foodRepository.save(saveRequest.toEntity(user));

        //// make url
        String url = PREFIX_URI + "/" + saveFood.getId();

        //// test food data for testing update feature
        FoodRequest updateRequest = createFoodRequest("update food", LocalDate.now());

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
                .andExpect(jsonPath("$.id").value(saveFood.getId()))
                .andExpect(jsonPath("$.name").value(updateRequest.getName()))
                .andExpect(jsonPath("$.startDate")
                        .value(updateRequest.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }
//
//    @Test
//    @DisplayName("음식 수정 - 실패(로그인 X)")
//    void updateFoodFailedWhenUserIsUnAuthenticated() throws Exception {
//    }
//
//    @Test
//    @DisplayName("음식 수정 - 실패(존재하지 않는 음식)")
//    void updateFoodFailedWhenNotExistFood() throws Exception {
//    }

    @Test
    @DisplayName("음식 삭제")
    void deleteFood() throws Exception {
        // given
        //// test food data in advance
        FoodRequest saveRequest = createFoodRequest();
        Food saveFood = foodRepository.save(saveRequest.toEntity(user));

        System.out.println(saveFood.getId());

        //// make url
        String url = PREFIX_URI + "/" + saveFood.getId();

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

//    @Test
//    @DisplayName("음식 삭제 - 실패(로그인 X)")
//    void deleteFoodFailedWhenUserIsUnAuthenticated() throws Exception {
//    }
//
//    @Test
//    @DisplayName("음식 삭제 - 실패(존재하지 않는 음식)")
//    void deleteFoodFailedWhenNotExistFood() throws Exception {
//    }
//
}
