package com.bank.management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class LoginRequest {

    @Getter
    @Setter
    @NotBlank(message = "Username is required")
    private String username;

    @Getter
    @Setter
    @NotBlank(message = "Password is required")
    private String password;
}