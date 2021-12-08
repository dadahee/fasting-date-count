package com.term.fastingdatecounter.global.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ErrorCode {
    METHOD_NOT_SUPPORTED("G01", "지원되지 않는 요청 메서드입니다."),
    ACCESS_DENIED("G02", "접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR("G03", "서버 에러로 요청을 처리하지 못했습니다."),

    NOT_FOUND_USER("G04", "유저 정보를 찾을 수 없습니다."),


    EMPTY_FOOD_NAME("F01", "음식명을 작성해주세요."),
    TOO_LONG_FOOD_NAME("F02", "음식명은 50자 이내여야 합니다."),
    NOT_FOUND_FOOD("F03", "음식 정보를 찾을 수 없습니다."),
    FUTURE_FOOD_START_DATE("F04", "단식 시작일은 현재까지만 선택할 수 있습니다."),
    TOO_LATE_FOOD_START_DATE("F05", "단식 시작일은 리뷰보다 앞선 날짜만 가능합니다."),


    FUTURE_REVIEW_DATE("R01", "미래 날짜는 리뷰를 남길 수 없습니다."),
    EMPTY_REVIEW_TITLE("R02", "리뷰 제목을 작성해주세요."),
    TOO_LONG_REVIEW_TITLE("R03", "리뷰 제목은 100자 이내여야 합니다."),
    EMPTY_REVIEW_CONTENT("R04", "리뷰 내용을 작성해주세요."),
    TOO_LONG_REVIEW_CONTENT("R05", "리뷰 내용은 500자 이내여야 합니다."),
    EMPTY_REVIEW_FASTED("R06", "단식 여부를 선택해주세요."),
    NOT_FOUND_REVIEW("R07", "리뷰 정보를 찾을 수 없습니다."),
    ALREADY_WRITTEN_REVIEW_DATE("R08", "해당 날짜에 이미 작성된 리뷰가 있습니다."),
    EARLIER_REVIEW_DATE("R09", "단식 시작일 이후의 리뷰만 남길 수 있습니다.")
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode of(String code) {
//        Map<String, String> codeInfo = Collections.unmodifiableMap(
//                Stream.of(values()).collect(Collectors.toMap(ErrorCode::getCode, ErrorCode::getMessage)));
//        return ErrorCode.valueOf(codeInfo.get(code));
        Map<String, ErrorCode> codeMap = Collections.unmodifiableMap(
                Stream.of(values()).collect(Collectors.toMap(ErrorCode::getCode, e -> e)));
        return codeMap.get(code);
    }
}
