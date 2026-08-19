package com.bank.management.dto.request;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CreateCustomerRequest {

    @Setter
    @Getter
    @NotBlank(message = "First name is required")
    private String firstName;

    @Setter
    @Getter
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Setter
    @Getter
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Setter
    @Getter
    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10 digit Indian mobile number"
    )
    private String phone;

    @Setter
    @Getter
    @NotBlank(message = "Address is required")
    private String address;

    @Setter
    @Getter
    private String dateOfBirth;

    @Setter
    @Getter
    private String username;

    @Setter
    @Getter
    private String password;

    public CreateCustomerRequest() {
    }

}