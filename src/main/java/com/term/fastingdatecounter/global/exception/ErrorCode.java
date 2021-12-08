package com.term.fastingdatecounter.global.exception;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ErrorCode {
    METHOD_NOT_SUPPORTED("G01", "Request method is not supported"),
    ACCESS_DENIED("G02", "No Authority : Access denied."),
    INTERNAL_SERVER_ERROR("G03", "Internal server error occurred"),

    NOT_FOUND_USER("G04", "Unable to retrieve user information"),


    EMPTY_FOOD_NAME("F01", "Food name is empty"),
    TOO_LONG_FOOD_NAME("F02", "Food name is too long"),
    NOT_FOUND_FOOD("F03", "Food does not exist"),
    FUTURE_FOOD_START_DATE("F04", "Future date is not allowed"),
    TOO_LATE_FOOD_START_DATE("F05", "Start date must be earlier than review date"),


    FUTURE_REVIEW_DATE("R01", "Future review date is not allowed"),
    EMPTY_REVIEW_TITLE("R02", "Review title is empty"),
    TOO_LONG_REVIEW_TITLE("R03", "Review title is too long"),
    EMPTY_REVIEW_CONTENT("R04", "Review content is empty"),
    TOO_LONG_REVIEW_CONTENT("R05", "Review content is too long"),
    EMPTY_REVIEW_FASTED("R06", "Whether to fast or not fasting is empty"),
    NOT_FOUND_REVIEW("R07", "Review does not exist"),
    ALREADY_WRITTEN_REVIEW_DATE("R08", "Review has been written on that date"),
    EARLIER_REVIEW_DATE("R09", "Review date should be later than the start date of fasting")
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
