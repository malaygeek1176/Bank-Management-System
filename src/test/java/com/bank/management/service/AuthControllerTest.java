package com.bank.management.service;

import com.bank.management.controller.AuthController;
import com.bank.management.dto.request.LoginRequest;
import com.bank.management.dto.response.LoginResponse;
import com.bank.management.security.JwtService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;


    @Test
    void login_shouldReturnJwtTokenForValidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("admin123");

        when(jwtService.generateToken("admin"))
                .thenReturn("test-jwt-token");

        ResponseEntity<LoginResponse> response =
                authController.login(request);

        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                "test-jwt-token",
                response.getBody().getToken()
        );

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(jwtService).generateToken("admin");
    }


    @Test
    void login_shouldThrowExceptionForInvalidCredentials() {

        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("wrong-password");

        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        )).thenThrow(
                new RuntimeException("Bad credentials")
        );

        assertThrows(
                RuntimeException.class,
                () -> authController.login(request)
        );

        verify(authenticationManager).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );

        verify(jwtService, never())
                .generateToken(anyString());
    }
}