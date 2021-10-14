package com.companyx.equity.error;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import lombok.extern.slf4j.Slf4j;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {
    @ExceptionHandler(value = ResponseVerificationException.class)
    protected ResponseEntity<Object> exception(ResponseVerificationException e) {
        String msg = "Remote resource interaction error. " + e.getMessage();
        log.error(msg);
        return new ResponseEntity<>(msg, HttpStatus.EXPECTATION_FAILED);
    }
}
