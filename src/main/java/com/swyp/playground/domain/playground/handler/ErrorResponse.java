package com.swyp.playground.domain.playground.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {

    public ErrorResponse(String httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    private final String httpStatus;
    private final String message;

}
