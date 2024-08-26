package com.example.gamehubbackend.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdServiceUnitTest {

    IdService idService = new IdService();

    @Test
    void testRandomId() {
        String id = idService.randomId();

        assertTrue(id != null && !id.isEmpty());
        assertTrue(id.matches("^[a-fA-F0-9\\-]{36}"));
    }

}