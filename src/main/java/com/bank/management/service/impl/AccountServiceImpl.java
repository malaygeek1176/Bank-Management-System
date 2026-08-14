package com.bank.management.service.impl;

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

    private final SecureRandom secureRandom = new SecureRandom();

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
    public Account createAccount(Long customerId, Long accountTypeId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Customer not found with id: " + customerId
                        ));

        AccountType accountType = accountTypeRepository.findById(accountTypeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account type not found with id: " + accountTypeId
                        ));

        Account account = new Account();

        account.setAccountNumber(generateUniqueAccountNumber());
        account.setCustomer(customer);
        account.setAccountType(accountType);

        //New Account amount is 0.00
        account.setBalance(BigDecimal.ZERO);

        //Status Automatically ACTIVE
        account.setStatus(AccountStatus.ACTIVE);

        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with id: " + id
                        ));
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {

        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountNumber
                        ));
    }

    @Override
    public List<Account> getAccountsByCustomer(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException(
                    "Customer not found with id: " + customerId
            );
        }

        return accountRepository.findByCustomerId(customerId);
    }

    private String generateUniqueAccountNumber() {

        String accountNumber;

        do {
            accountNumber = generateAccountNumber();

            //duplicate check karte hain
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    private String generateAccountNumber() {

        StringBuilder accountNumber = new StringBuilder();

        for (int i = 0; i < 12; i++) {

            //Account Number Generation
            accountNumber.append(secureRandom.nextInt(10));
        }

        return accountNumber.toString();
    }
}

//flow

//Customer ID
//     ↓
//CustomerRepository
//     ↓
//Customer exists?
//     ↓
//    YES
//     ↓
//Account Type ID
//     ↓
//AccountTypeRepository
//     ↓
//Account Type exists?
//     ↓
//    YES
//     ↓
//Generate Account Number
//     ↓
//Balance = 0
//     ↓
//Status = ACTIVE
//     ↓
//Save Account