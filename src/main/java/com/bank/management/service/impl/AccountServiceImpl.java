package com.bank.management.service.impl;

import com.bank.management.dto.response.AccountResponse;
import com.bank.management.entity.Account;
import com.bank.management.entity.AccountStatus;
import com.bank.management.entity.AccountType;
import com.bank.management.entity.Customer;
import com.bank.management.exception.ResourceNotFoundException;
import com.bank.management.repository.AccountRepository;
import com.bank.management.repository.AccountTypeRepository;
import com.bank.management.repository.CustomerRepository;
import com.bank.management.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final AccountTypeRepository accountTypeRepository;

    private final SecureRandom secureRandom =
            new SecureRandom();

    public AccountServiceImpl(
            AccountRepository accountRepository,
            CustomerRepository customerRepository,
            AccountTypeRepository accountTypeRepository) {

        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    @Transactional
    public AccountResponse createAccount(
            Long customerId,
            Long accountTypeId) {

        Customer customer =
                customerRepository.findById(customerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Customer not found with id: "
                                                + customerId
                                ));

        AccountType accountType =
                accountTypeRepository.findById(accountTypeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account type not found with id: "
                                                + accountTypeId
                                ));

        Account account = new Account();

        account.setAccountNumber(
                generateUniqueAccountNumber()
        );

        account.setCustomer(customer);
        account.setAccountType(accountType);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);

        Account savedAccount =
                accountRepository.save(account);

        return mapToResponse(savedAccount);
    }

    @Override
    public AccountResponse getAccountById(Long id) {

        Account account =
                accountRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found with id: "
                                                + id
                                ));

        return mapToResponse(account);
    }

    @Override
    public AccountResponse getAccountByNumber(
            String accountNumber) {

        Account account =
                accountRepository
                        .findByAccountNumber(accountNumber)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Account not found: "
                                                + accountNumber
                                ));

        return mapToResponse(account);
    }

    @Override
    public List<AccountResponse> getAccountsByCustomer(
            Long customerId) {

        if (!customerRepository.existsById(customerId)) {

            throw new ResourceNotFoundException(
                    "Customer not found with id: "
                            + customerId
            );
        }

        return accountRepository
                .findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AccountResponse mapToResponse(
            Account account) {

        Customer customer = account.getCustomer();

        AccountType accountType =
                account.getAccountType();

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                customer.getId(),
                customer.getFirstName()
                        + " "
                        + customer.getLastName(),
                accountType.getId(),
                accountType.getName(),
                account.getBalance(),
                account.getStatus().name()
        );
    }

    private String generateUniqueAccountNumber() {

        String accountNumber;

        do {
            accountNumber =
                    generateAccountNumber();

        } while (
                accountRepository
                        .existsByAccountNumber(accountNumber)
        );

        return accountNumber;
    }

    private String generateAccountNumber() {

        StringBuilder accountNumber =
                new StringBuilder();

        for (int i = 0; i < 12; i++) {

            accountNumber.append(
                    secureRandom.nextInt(10)
            );
        }

        return accountNumber.toString();
    }
}