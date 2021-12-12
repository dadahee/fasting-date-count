package com.term.fastingdatecounter.domain.review.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {

    @Test
    @DisplayName("리뷰 저장 - 성공")
    void save() {
    }

    @Test
    @DisplayName("리뷰 저장 - 실패(존재하지 않는 유저)")
    void saveFailedWhenNotExistUser() {
    }

    @Test
    @DisplayName("리뷰 저장 - 실패(존재하지 않는 음식)")
    void saveFailedWhenNotExistFood() {
    }

    @Test
    @DisplayName("리뷰 저장 - 실패(음식 작성자와 다른 유저의 요청)")
    void saveFailedWhenUserWithoutAuthority() {
    }

    @Test
    @DisplayName("리뷰 저장 - 실패(리뷰 날짜가 잘못됨)")
    void saveFailedWhenDateIsInvalid() {
    }

    @Test
    @DisplayName("리뷰 수정 - 성공")
    void update() {
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(존재하지 않는 유저)")
    void updateFailedWhenNotExistUser() {
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(존재하지 않는 음식)")
    void updateFailedWhenNotExistFood() {
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(존재하지 않는 리뷰)")
    void updateFailedWhenNotExistReview() {
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(작성자와 다른 유저의 요청)")
    void updateFailedWhenUserWithoutAuthority() {
    }

    @Test
    @DisplayName("리뷰 수정 - 실패(리뷰 날짜가 잘못됨)")
    void updateFailedWhenDateIsInvalid() {
    }

    @Test
    @DisplayName("리뷰 삭제 - 성공")
    void delete() {
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패(존재하지 않는 유저)")
    void deleteFailedWhenNotExistUser() {
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패(존재하지 않는 음식)")
    void deleteFailedWhenNotExistFood() {
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패(존재하지 않는 리뷰)")
    void deleteFailedWhenNotExistReview() {
    }

    @Test
    @DisplayName("리뷰 삭제 - 실패(작성자와 다른 유저의 요청)")
    void deleteFailedWhenUserWithoutAuthority() {
    }
}