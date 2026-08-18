package com.bank.management.controller;

import com.bank.management.entity.Transaction;
import com.bank.management.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Deposit money
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {

        return ResponseEntity.ok(
                transactionService.deposit(
                        accountNumber,
                        amount,
                        description
                )
        );
    }

    // Withdraw money
    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {

        return ResponseEntity.ok(
                transactionService.withdraw(
                        accountNumber,
                        amount,
                        description
                )
        );
    }

    // Transfer money
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @RequestParam String senderAccountNumber,
            @RequestParam String receiverAccountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {

        return ResponseEntity.ok(
                transactionService.transfer(
                        senderAccountNumber,
                        receiverAccountNumber,
                        amount,
                        description
                )
        );
    }

    // Transaction history
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<Transaction>> getAccountTransactions(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                transactionService.getAccountTransactions(accountNumber)
        );
    }
}