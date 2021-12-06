package com.term.fastingdatecounter.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException{
    private final HttpStatus status;
    private final String code;

    public ServiceException(HttpStatus status, ErrorCode errorCode) {
        this.status = status;
        this.code = errorCode.getCode();
    }
}
