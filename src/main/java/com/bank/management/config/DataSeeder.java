package com.bank.management.config;

import com.bank.management.entity.AccountType;
import com.bank.management.entity.TransactionType;
import com.bank.management.repository.AccountTypeRepository;
import com.bank.management.repository.TransactionTypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedMasterData(
            AccountTypeRepository accountTypeRepository,
            TransactionTypeRepository transactionTypeRepository) {

        return args -> {

            seedAccountTypes(accountTypeRepository);
            seedTransactionTypes(transactionTypeRepository);
        };
    }

    private void seedAccountTypes(
            AccountTypeRepository repository) {

        if (repository.findByName("SAVINGS").isEmpty()) {

            AccountType savings = new AccountType();

            savings.setName("SAVINGS");
            savings.setDescription("Savings Bank Account");
            savings.setMinimumBalance(
                    new BigDecimal("1000.00")
            );

            repository.save(savings);
        }

        if (repository.findByName("CURRENT").isEmpty()) {

            AccountType current = new AccountType();

            current.setName("CURRENT");
            current.setDescription("Current Bank Account");
            current.setMinimumBalance(
                    new BigDecimal("0.00")
            );

            repository.save(current);
        }
    }

    private void seedTransactionTypes(
            TransactionTypeRepository repository) {

        //Iska matlab application har restart par duplicate data create nahi karegi.
        if (repository.findByName("DEPOSIT").isEmpty()) {

            TransactionType deposit = new TransactionType();

            deposit.setName("DEPOSIT");
            deposit.setDescription(
                    "Money deposited into account"
            );

            repository.save(deposit);
        }

        if (repository.findByName("WITHDRAWAL").isEmpty()) {

            TransactionType withdrawal = new TransactionType();

            withdrawal.setName("WITHDRAWAL");
            withdrawal.setDescription(
                    "Money withdrawn from account"
            );

            repository.save(withdrawal);
        }

        if (repository.findByName("TRANSFER").isEmpty()) {

            TransactionType transfer = new TransactionType();

            transfer.setName("TRANSFER");
            transfer.setDescription(
                    "Money transferred between accounts"
            );

            repository.save(transfer);
        }
    }
}

//Spring Boot starts
//       ↓
//DataSeeder executes
//       ↓
//Check SAVINGS
//       ↓
//Not found → Create
//       ↓
//Check CURRENT
//       ↓
//Not found → Create
//       ↓
//Check DEPOSIT
//       ↓
//Not found → Create
//       ↓
//Check WITHDRAWAL
//       ↓
//Not found → Create
//       ↓
//Check TRANSFER
//       ↓
//Not found → Create