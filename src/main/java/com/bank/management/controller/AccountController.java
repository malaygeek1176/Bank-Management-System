package com.bank.management.controller;
import com.bank.management.dto.request.CreateAccountRequest;
import com.bank.management.dto.response.AccountResponse;
import com.bank.management.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request.getCustomerId(),request.getAccountTypeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(
                accountService.getAccountById(id)
        );
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(
                accountService.getAccountByNumber(accountNumber)
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponse>>
    getAccountsByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(
                accountService.getAccountsByCustomer(customerId));
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(
            @PathVariable String accountNumber) {

        AccountResponse account =
                accountService.getAccountByNumber(accountNumber);

        return ResponseEntity.ok(account.getBalance());
    }
}