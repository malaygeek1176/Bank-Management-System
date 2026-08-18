package com.bank.management.dto.request;

import lombok.Getter;
import lombok.Setter;

public class CreateCustomerRequest {

    @Setter
    @Getter
    private String firstName;

    @Setter
    @Getter
    private String lastName;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String phone;

    @Setter
    @Getter
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