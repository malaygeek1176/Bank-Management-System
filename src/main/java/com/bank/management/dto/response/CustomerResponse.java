package com.bank.management.dto.response;

import lombok.Getter;

public class CustomerResponse {

    @Getter
    private Long id;

    @Getter
    private String firstName;

    @Getter
    private String lastName;

    @Getter
    private String email;

    @Getter
    private String phone;

    @Getter
    private String address;

    public CustomerResponse() {
    }

    public CustomerResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phone,
            String address) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

}