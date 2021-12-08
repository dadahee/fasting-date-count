package com.term.fastingdatecounter.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // business error
    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<ErrorResponse> handleServiceException(ServiceException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(ErrorResponse.of(ErrorCode.of(exception.getCode())));
    }

    // javax.validation.valid or @Validated binding error
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        final ErrorCode errorCode = ErrorCode.valueOf(exception.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.of(errorCode));
    }

    // request method is not supported error
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        final ErrorCode errorCode = ErrorCode.METHOD_NOT_SUPPORTED;
        return ResponseEntity.badRequest().body(ErrorResponse.of(errorCode));
    }

    // access denied error
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        final ErrorResponse response = ErrorResponse.of(ErrorCode.ACCESS_DENIED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

}
