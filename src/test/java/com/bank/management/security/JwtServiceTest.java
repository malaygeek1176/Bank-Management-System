package com.bank.management.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String TEST_SECRET =
            "bank-management-system-test-secret-key-2026-very-secure";


    @BeforeEach
    void setUp() {

        jwtService = new JwtService(TEST_SECRET);
    }


    @Test
    void generateToken_shouldGenerateValidToken() {

        String token =
                jwtService.generateToken("testuser");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }


    @Test
    void extractUsername_shouldReturnCorrectUsername() {

        String token =
                jwtService.generateToken("testuser");

        String username =
                jwtService.extractUsername(token);

        assertEquals("testuser", username);
    }


    @Test
    void isTokenValid_shouldReturnTrueForCorrectUsername() {

        String token =
                jwtService.generateToken("testuser");

        boolean result =
                jwtService.isTokenValid(
                        token,
                        "testuser"
                );

        assertTrue(result);
    }


    @Test
    void isTokenValid_shouldReturnFalseForWrongUsername() {

        String token =
                jwtService.generateToken("testuser");

        boolean result =
                jwtService.isTokenValid(
                        token,
                        "wronguser"
                );

        assertFalse(result);
    }
}