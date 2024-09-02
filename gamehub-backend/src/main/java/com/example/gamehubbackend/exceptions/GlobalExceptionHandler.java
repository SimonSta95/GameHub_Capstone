package com.example.gamehubbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomErrorMessage handleGameNotFoundException(GameNotFoundException e) {
        return new CustomErrorMessage(
                e.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CustomErrorMessage handleUserNotFoundException(UserNotFoundException e) {
        return new CustomErrorMessage(
                e.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  CustomErrorMessage handleNoteNotFoundException(NoteNotFoundException e) {
        return new CustomErrorMessage(
                e.getMessage(),
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value()
        );
    }
}
