package com.bank.management.service;

import com.bank.management.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {

    Transaction deposit(
            String accountNumber,
            BigDecimal amount,
            String description
    );

    Transaction withdraw(
            String accountNumber,
            BigDecimal amount,
            String description
    );

    Transaction transfer(
            String senderAccountNumber,
            String receiverAccountNumber,
            BigDecimal amount,
            String description
    );

    List<Transaction> getAccountTransactions(
            String accountNumber
    );
}