package com.example.gamehubbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice  // Global exception handler for all controllers
public class GlobalExceptionHandler {

    /**
     * Handles GameNotFoundException.
     * Returns a custom error message with a 404 Not Found status.
     *
     * @param e the thrown GameNotFoundException
     * @return CustomErrorMessage with details about the error
     */
    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)  // Respond with 404 status
    public CustomErrorMessage handleGameNotFoundException(GameNotFoundException e) {
        return new CustomErrorMessage(
                e.getMessage(),  // Error message from the exception
                LocalDateTime.now(),  // Current timestamp
                HttpStatus.NOT_FOUND.value()  // 404 status code
        );
    }

    /**
     * Handles UserNotFoundException.
     * Returns a custom error message with a 404 Not Found status.
     *
     * @param e the thrown UserNotFoundException
     * @return CustomErrorMessage with details about the error
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)  // Respond with 404 status
    public CustomErrorMessage handleUserNotFoundException(UserNotFoundException e) {
        return new CustomErrorMessage(
                e.getMessage(),  // Error message from the exception
                LocalDateTime.now(),  // Current timestamp
                HttpStatus.NOT_FOUND.value()  // 404 status code
        );
    }

    /**
     * Handles NoteNotFoundException.
     * Returns a custom error message with a 404 Not Found status.
     *
     * @param e the thrown NoteNotFoundException
     * @return CustomErrorMessage with details about the error
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)  // Respond with 404 status
    public CustomErrorMessage handleNoteNotFoundException(NoteNotFoundException e) {
        return new CustomErrorMessage(
                e.getMessage(),  // Error message from the exception
                LocalDateTime.now(),  // Current timestamp
                HttpStatus.NOT_FOUND.value()  // 404 status code
        );
    }

    /**
     * Handles ReviewNotFoundException.
     * Returns a custom error message with a 404 Not Found status.
     *
     * @param e the thrown ReviewNotFoundException
     * @return CustomErrorMessage with details about the error
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)  // Respond with 404 status
    public CustomErrorMessage handleReviewNotFoundException(ReviewNotFoundException e) {
        return new CustomErrorMessage(
                e.getMessage(),  // Error message from the exception
                LocalDateTime.now(),  // Current timestamp
                HttpStatus.NOT_FOUND.value()  // 404 status code
        );
    }
}
