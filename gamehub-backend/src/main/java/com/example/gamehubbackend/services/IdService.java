package com.example.gamehubbackend.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {

    /**
     * Generates a random unique identifier (UUID).
     *
     * @return a randomly generated UUID as a String
     */
    public String randomId() {
        return UUID.randomUUID().toString();
    }
}
