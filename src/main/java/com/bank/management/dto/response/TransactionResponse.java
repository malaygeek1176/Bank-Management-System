package com.bank.management.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;

    private String transactionReference;

    private String accountNumber;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal balanceAfter;

    private String description;

    private String status;

    private LocalDateTime createdAt;

    public TransactionResponse() {
    }

    public TransactionResponse(
            Long id,
            String transactionReference,
            String accountNumber,
            String transactionType,
            BigDecimal amount,
            BigDecimal balanceAfter,
            String description,
            String status,
            LocalDateTime createdAt) {

        this.id = id;
        this.transactionReference = transactionReference;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}