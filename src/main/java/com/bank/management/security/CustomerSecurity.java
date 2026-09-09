package com.bank.management.security;

import com.bank.management.entity.Customer;
import com.bank.management.repository.CustomerRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("customerSecurity")
public class CustomerSecurity {

    private final CustomerRepository customerRepository;

    public CustomerSecurity(
            CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    public boolean isOwner(
            Long customerId,
            Authentication authentication
    ) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {
            return false;
        }

        String username =
                authentication.getName();

        return customerRepository
                .findByUserUsername(username)
                .map(Customer::getId)
                .map(customerId::equals)
                .orElse(false);
    }
}