package com.example.gamehubbackend.exceptions;

import java.time.LocalDateTime;

/**
 * A record class that represents a custom error message.
 * This class is used to send structured error messages to the client.
 *
 * @param message    the error message
 * @param timestamp  the time the error occurred
 * @param statusCode the HTTP status code associated with the error
 */
public record CustomErrorMessage(
        String message,         // The error message to be displayed
        LocalDateTime timestamp, // The time the error occurred
        int statusCode           // The associated HTTP status code
) {}
