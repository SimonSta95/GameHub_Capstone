package com.example.gamehubbackend.exceptions;

import java.time.LocalDateTime;

public record CustomErrorMessage(
        String message,
        LocalDateTime timestamp,
        int statusCode
) {}
