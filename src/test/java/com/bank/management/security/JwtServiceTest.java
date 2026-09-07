package com.bank.management.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void generateToken_shouldCreateToken() {

        String token =
                jwtService.generateToken("admin");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {

        String token =
                jwtService.generateToken("admin");

        String username =
                jwtService.extractUsername(token);

        assertEquals("admin", username);
    }

    @Test
    void isTokenValid_shouldReturnTrueForCorrectUsername() {

        String token =
                jwtService.generateToken("admin");

        boolean valid =
                jwtService.isTokenValid(token, "admin");

        assertTrue(valid);
    }

    @Test
    void isTokenValid_shouldReturnFalseForWrongUsername() {

        String token =
                jwtService.generateToken("admin");

        boolean valid =
                jwtService.isTokenValid(token, "customer");

        assertFalse(valid);
    }
}