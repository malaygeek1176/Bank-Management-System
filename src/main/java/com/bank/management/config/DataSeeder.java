package com.bank.management.config;

import com.bank.management.entity.AccountType;
import com.bank.management.entity.Role;
import com.bank.management.entity.TransactionType;
import com.bank.management.entity.User;
import com.bank.management.repository.AccountTypeRepository;
import com.bank.management.repository.RoleRepository;
import com.bank.management.repository.TransactionTypeRepository;
import com.bank.management.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedMasterData(
            AccountTypeRepository accountTypeRepository,
            TransactionTypeRepository transactionTypeRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {

        return args -> {

            // =========================
            // ROLES
            // =========================

            Role customerRole =
                    roleRepository.findByName("CUSTOMER")
                            .orElseGet(() -> {
                                Role role = new Role();
                                role.setName("CUSTOMER");
                                return roleRepository.save(role);
                            });

            Role adminRole =
                    roleRepository.findByName("ADMIN")
                            .orElseGet(() -> {
                                Role role = new Role();
                                role.setName("ADMIN");
                                return roleRepository.save(role);
                            });


            // =========================
            // DEFAULT ADMIN USER
            // =========================

            if (!userRepository.existsByUsername("admin")) {

                User admin = new User();

                admin.setUsername("admin");

                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );

                admin.setRole(adminRole);

                admin.setEnabled(true);

                userRepository.save(admin);
            }


            // =========================
            // ACCOUNT TYPES
            // =========================

            seedAccountTypes(accountTypeRepository);


            // =========================
            // TRANSACTION TYPES
            // =========================

            seedTransactionTypes(transactionTypeRepository);
        };
    }


    private void seedAccountTypes(
            AccountTypeRepository repository
    ) {

        if (repository.findByName("SAVINGS").isEmpty()) {

            AccountType savings = new AccountType();

            savings.setName("SAVINGS");

            savings.setDescription(
                    "Savings Bank Account"
            );

            savings.setMinimumBalance(
                    new BigDecimal("1000.00")
            );

            repository.save(savings);
        }


        if (repository.findByName("CURRENT").isEmpty()) {

            AccountType current = new AccountType();

            current.setName("CURRENT");

            current.setDescription(
                    "Current Bank Account"
            );

            current.setMinimumBalance(
                    new BigDecimal("0.00")
            );

            repository.save(current);
        }
    }


    private void seedTransactionTypes(
            TransactionTypeRepository repository
    ) {

        if (repository.findByName("DEPOSIT").isEmpty()) {

            TransactionType deposit = new TransactionType();

            deposit.setName("DEPOSIT");

            deposit.setDescription(
                    "Money deposited into account"
            );

            repository.save(deposit);
        }


        if (repository.findByName("WITHDRAWAL").isEmpty()) {

            TransactionType withdrawal =
                    new TransactionType();

            withdrawal.setName("WITHDRAWAL");

            withdrawal.setDescription(
                    "Money withdrawn from account"
            );

            repository.save(withdrawal);
        }


        if (repository.findByName("TRANSFER").isEmpty()) {

            TransactionType transfer =
                    new TransactionType();

            transfer.setName("TRANSFER");

            transfer.setDescription(
                    "Money transferred between accounts"
            );

            repository.save(transfer);
        }
    }
}