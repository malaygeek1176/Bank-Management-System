package com.bank.management.service;

import com.bank.management.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(
            Long customerId,
            Long accountTypeId
    );

    AccountResponse getAccountById(Long id);

    AccountResponse getAccountByNumber(
            String accountNumber
    );

    List<AccountResponse> getAccountsByCustomer(
            Long customerId
    );
}