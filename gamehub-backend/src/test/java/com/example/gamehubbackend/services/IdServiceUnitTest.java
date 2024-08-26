package com.example.gamehubbackend.services;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class IdServiceUnitTest {

    IdService idService = new IdService();

    @Test
    void testRandomId() {
        UUID uuid = UUID.randomUUID();

        try (MockedStatic<UUID> mockedUUID = mockStatic(UUID.class)) {
            mockedUUID.when(UUID::randomUUID).thenReturn(uuid);

            String actual = idService.randomId();

            String expected = uuid.toString();
            mockedUUID.verify(UUID::randomUUID);
            assertEquals(expected, actual);
        }
    }

}