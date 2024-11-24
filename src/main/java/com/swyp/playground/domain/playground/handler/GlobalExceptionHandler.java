package com.swyp.playground.exception;

import com.swyp.playground.domain.playground.handler.ErrorResponse;
import com.swyp.playground.domain.playground.exception.PlaygroundNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //검색한 놀이터가 없을 경우
    @ExceptionHandler(PlaygroundNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlaygroundNotFoundException(PlaygroundNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(HttpStatus.NOT_FOUND.toString(), ex.getMessage())
        );
    }
}
