package com.example.gamehubbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
     * Handles validation errors from invalid method arguments (e.g., @Valid).
     * Returns a map of field errors with their corresponding messages.
     *
     * @param e the thrown MethodArgumentNotValidException
     * @return Map of field names and corresponding error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Respond with 400 Bad Request
    @ResponseBody  // Include the response in the body
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();  // Create a map to hold field errors
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();  // Get the field gameTitle that caused the error
            String errorMessage = error.getDefaultMessage();  // Get the error message
            errors.put(fieldName, errorMessage);  // Add field and message to the map
        });
        return errors;
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
