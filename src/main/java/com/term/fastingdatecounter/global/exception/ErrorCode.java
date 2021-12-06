package com.term.fastingdatecounter.global.exception;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum ErrorCode {
    METHOD_NOT_SUPPORTED("G01", "Request method is not supported"),
    ACCESS_DENIED("G02", "No Authority : Access denied."),
    INTERNAL_SERVER_ERROR("G03", "Internal server error occurred"),


    EMPTY_FOOD_NAME("F01", "Food name is empty"),
    TOO_LONG_FOOD_NAME("F02", "Food name is too long"),
    NOT_FOUND_FOOD("F03", "Food does not exist"),
    NOT_FOUND_USER("F04", "Unable to retrieve user information"),
    FUTURE_FOOD_START_DATE("F05", "Future date is not allowed"),
    TOO_LATE_FOOD_START_DATE("F06", "Start date must be earlier than review date")
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorCode of(String code) {
        Map<String, String> codeInfo = Collections.unmodifiableMap(
                Stream.of(values()).collect(Collectors.toMap(ErrorCode::getCode, ErrorCode::getMessage)));
        return ErrorCode.valueOf(codeInfo.get(code));
    }
}
