package com.bank.management.controller;

import com.bank.management.dto.request.TransferRequest;
import com.bank.management.dto.response.TransactionResponse;
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

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {

        Transaction transaction =
                transactionService.deposit(
                        accountNumber,
                        amount,
                        description
                );

        return ResponseEntity.ok(toResponse(transaction));
    }


    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @RequestParam String accountNumber,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {

        Transaction transaction =
                transactionService.withdraw(
                        accountNumber,
                        amount,
                        description
                );

        return ResponseEntity.ok(toResponse(transaction));
    }


    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @RequestBody TransferRequest request) {

        Transaction transaction =
                transactionService.transfer(
                        request.getSenderAccountNumber(),
                        request.getReceiverAccountNumber(),
                        request.getAmount(),
                        request.getDescription()
                );

        return ResponseEntity.ok(toResponse(transaction));
    }


    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(
            @PathVariable String accountNumber) {

        List<TransactionResponse> response =
                transactionService
                        .getAccountTransactions(accountNumber)
                        .stream()
                        .map(this::toResponse)
                        .toList();

        return ResponseEntity.ok(response);
    }


    private TransactionResponse toResponse(Transaction transaction) {

        TransactionResponse response = new TransactionResponse();

        response.setId(transaction.getId());

        response.setTransactionReference(
                transaction.getTransactionReference()
        );

        response.setAccountNumber(
                transaction.getAccount().getAccountNumber()
        );

        response.setTransactionType(
                transaction.getTransactionType().getName()
        );

        response.setAmount(transaction.getAmount());

        response.setBalanceAfter(transaction.getBalanceAfter());

        response.setDescription(transaction.getDescription());

        response.setStatus(transaction.getStatus().name());

        response.setCreatedAt(transaction.getCreatedAt());

        return response;
    }
}