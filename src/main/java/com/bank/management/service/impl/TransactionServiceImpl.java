package com.bank.management.service.impl;

import com.bank.management.entity.Account;
import com.bank.management.entity.AccountStatus;
import com.bank.management.entity.Transaction;
import com.bank.management.entity.TransactionStatus;
import com.bank.management.entity.TransactionType;
import com.bank.management.exception.ResourceNotFoundException;
import com.bank.management.repository.AccountRepository;
import com.bank.management.repository.TransactionRepository;
import com.bank.management.repository.TransactionTypeRepository;
import com.bank.management.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionTypeRepository transactionTypeRepository;

    private final SecureRandom secureRandom = new SecureRandom();

    public TransactionServiceImpl(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionTypeRepository transactionTypeRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Override
    @Transactional
    public Transaction deposit(
            String accountNumber,
            BigDecimal amount,
            String description) {

        validateAmount(amount);

        Account account = getActiveAccount(accountNumber);

        TransactionType transactionType =
                transactionTypeRepository.findByName("DEPOSIT")
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction type DEPOSIT not found"
                                ));

        BigDecimal newBalance =
                account.getBalance().add(amount);

        account.setBalance(newBalance);

        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setTransactionReference(
                generateTransactionReference()
        );

        transaction.setAccount(account);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(description);
        transaction.setStatus(TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public Transaction withdraw(
            String accountNumber,
            BigDecimal amount,
            String description) {

        validateAmount(amount);

        Account account = getActiveAccount(accountNumber);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient account balance"
            );
        }

        TransactionType transactionType =
                transactionTypeRepository.findByName("WITHDRAWAL")
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction type WITHDRAWAL not found"
                                ));

        BigDecimal newBalance =
                account.getBalance().subtract(amount);

        account.setBalance(newBalance);

        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setTransactionReference(
                generateTransactionReference()
        );

        transaction.setAccount(account);
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);
        transaction.setBalanceAfter(newBalance);
        transaction.setDescription(description);
        transaction.setStatus(TransactionStatus.SUCCESS);

        return transactionRepository.save(transaction);
    }

    //TransactionService transfer method implemented by Malay
    @Override
    @Transactional
    public Transaction transfer(
            String senderAccountNumber,
            String receiverAccountNumber,
            BigDecimal amount,
            String description) {

        validateAmount(amount);

        if (senderAccountNumber.equals(receiverAccountNumber)) {
            throw new IllegalArgumentException(
                    "Sender and receiver accounts cannot be the same"
            );
        }

        Account sender = accountRepository
                .findByAccountNumberForUpdate(senderAccountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Sender account not found: "
                                        + senderAccountNumber
                        ));

        Account receiver = accountRepository
                .findByAccountNumberForUpdate(receiverAccountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Receiver account not found: "
                                        + receiverAccountNumber
                        ));

        if (sender.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Sender account is not active"
            );
        }

        if (receiver.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Receiver account is not active"
            );
        }

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient account balance"
            );
        }

        TransactionType transactionType =
                transactionTypeRepository.findByName("TRANSFER")
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Transaction type TRANSFER not found"
                                ));

        BigDecimal senderNewBalance =
                sender.getBalance().subtract(amount);

        BigDecimal receiverNewBalance =
                receiver.getBalance().add(amount);

        sender.setBalance(senderNewBalance);
        receiver.setBalance(receiverNewBalance);

        accountRepository.save(sender);
        accountRepository.save(receiver);

        Transaction senderTransaction = new Transaction();

        senderTransaction.setTransactionReference(
                generateTransactionReference()
        );

        senderTransaction.setAccount(sender);
        senderTransaction.setTransactionType(transactionType);
        senderTransaction.setAmount(amount);
        senderTransaction.setBalanceAfter(senderNewBalance);
        senderTransaction.setDescription(
                "Transfer to " + receiverAccountNumber
                        + " - " + description
        );
        senderTransaction.setStatus(TransactionStatus.SUCCESS);

        Transaction receiverTransaction = new Transaction();

        receiverTransaction.setTransactionReference(
                generateTransactionReference()
        );

        receiverTransaction.setAccount(receiver);
        receiverTransaction.setTransactionType(transactionType);
        receiverTransaction.setAmount(amount);
        receiverTransaction.setBalanceAfter(receiverNewBalance);
        receiverTransaction.setDescription(
                "Transfer from " + senderAccountNumber
                        + " - " + description
        );
        receiverTransaction.setStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(senderTransaction);
        transactionRepository.save(receiverTransaction);

        return senderTransaction;
    }

    @Override
    public List<Transaction> getAccountTransactions(
            String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountNumber
                        ));

        return transactionRepository
                .findByAccountIdOrderByCreatedAtDesc(
                        account.getId()
                );
    }

    private Account getActiveAccount(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountNumber
                        ));

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Account is not active"
            );
        }

        return account;
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Transaction amount must be greater than zero"
            );
        }
    }

    private String generateTransactionReference() {

        String reference;

        do {
            reference = "TXN-" +
                    System.currentTimeMillis() +
                    "-" +
                    secureRandom.nextInt(10000);

        } while (
                transactionRepository
                        .findByTransactionReference(reference)
                        .isPresent()
        );

        return reference;
    }

}

//Deposit ka complete flow

//Deposit ₹5,000
//      ↓
//Amount valid?
//      ↓
//Account exists?
//      ↓
//Account ACTIVE?
//      ↓
//Find DEPOSIT type
//      ↓
//Current Balance + ₹5,000
//      ↓
//Update Account
//      ↓
//Create Transaction
//      ↓
//SUCCESS