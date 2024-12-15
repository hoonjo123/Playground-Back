package com.swyp.playground.domain.playground.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlaygroundNotFoundException extends RuntimeException {
    public PlaygroundNotFoundException(String message) {
        super(message);
    }
}
