package com.bank.management.controller;

import com.bank.management.dto.request.CreateAccountRequest;
import com.bank.management.dto.response.AccountResponse;
import com.bank.management.service.AccountService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(
            AccountService accountService
    ) {
        this.accountService = accountService;
    }

    /*
     * Create Account
     *
     * ADMIN can create an account for any customer.
     * CUSTOMER can create an account only for themselves.
     */
    @PostMapping
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "@customerSecurity.isOwner(#request.customerId, authentication)"
    )
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {

        AccountResponse response =
                accountService.createAccount(
                        request.getCustomerId(),
                        request.getAccountTypeId()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /*
     * Get account by ID
     *
     * ADMIN -> any account
     * CUSTOMER -> only own account
     */
    @GetMapping("/{id}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "@accountSecurity.isOwner(#id, authentication)"
    )
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                accountService.getAccountById(id)
        );
    }

    /*
     * Get account by account number
     *
     * ADMIN -> any account
     * CUSTOMER -> only own account
     */
    @GetMapping("/number/{accountNumber}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "@accountSecurity.isAccountNumberOwner(" +
                    "#accountNumber, authentication)"
    )
    public ResponseEntity<AccountResponse> getAccountByNumber(
            @PathVariable String accountNumber
    ) {

        return ResponseEntity.ok(
                accountService.getAccountByNumber(
                        accountNumber
                )
        );
    }

    /*
     * Get all accounts of a customer
     *
     * ADMIN -> any customer
     * CUSTOMER -> only their own customer ID
     */
    @GetMapping("/customer/{customerId}")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "@accountSecurity.isCustomerAccountOwner(" +
                    "#customerId, authentication)"
    )
    public ResponseEntity<List<AccountResponse>> getAccountsByCustomer(
            @PathVariable Long customerId
    ) {

        return ResponseEntity.ok(
                accountService.getAccountsByCustomer(
                        customerId
                )
        );
    }

    /*
     * Get account balance
     *
     * Existing endpoint is preserved:
     * /api/accounts/{accountNumber}/balance
     *
     * ADMIN -> any account
     * CUSTOMER -> only own account
     */
    @GetMapping("/{accountNumber}/balance")
    @PreAuthorize(
            "hasRole('ADMIN') or " +
                    "@accountSecurity.isAccountNumberOwner(" +
                    "#accountNumber, authentication)"
    )
    public ResponseEntity<BigDecimal> getAccountBalance(
            @PathVariable String accountNumber
    ) {

        AccountResponse account =
                accountService.getAccountByNumber(
                        accountNumber
                );

        return ResponseEntity.ok(
                account.getBalance()
        );
    }
}