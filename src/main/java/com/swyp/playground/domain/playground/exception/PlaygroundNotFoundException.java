package com.swyp.playground.domain.playground.exception;

public class PlaygroundNotFoundException extends RuntimeException {
    public PlaygroundNotFoundException(String message) {
        super(message);
    }
}
