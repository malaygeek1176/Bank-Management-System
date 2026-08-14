package com.bank.management.service;

import com.bank.management.entity.Account;

import java.util.List;

public interface AccountService {

    Account createAccount(Long customerId, Long accountTypeId);

    Account getAccountById(Long id);

    Account getAccountByNumber(String accountNumber);

    List<Account> getAccountsByCustomer(Long customerId);
}