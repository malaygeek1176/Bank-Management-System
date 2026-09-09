package com.bank.management.security;

import com.bank.management.repository.AccountRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("accountSecurity")
public class AccountSecurity {

    private final AccountRepository accountRepository;

    public AccountSecurity(
            AccountRepository accountRepository
    ) {
        this.accountRepository = accountRepository;
    }

    /*
     * Check whether the logged-in customer owns
     * the account with the given account ID.
     */
    public boolean isOwner(
            Long accountId,
            Authentication authentication
    ) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return false;
        }

        String username =
                authentication.getName();

        return accountRepository
                .findByIdAndCustomerUserUsername(
                        accountId,
                        username
                )
                .isPresent();
    }

    /*
     * Check whether the logged-in customer owns
     * the account with the given account number.
     */
    public boolean isAccountNumberOwner(
            String accountNumber,
            Authentication authentication
    ) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return false;
        }

        String username =
                authentication.getName();

        return accountRepository
                .findByAccountNumberAndCustomerUserUsername(
                        accountNumber,
                        username
                )
                .isPresent();
    }

    /*
     * Check whether the logged-in customer owns
     * at least one account belonging to the given customer ID.
     */
    public boolean isCustomerAccountOwner(
            Long customerId,
            Authentication authentication
    ) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return false;
        }

        String username =
                authentication.getName();

        return accountRepository
                .findByCustomerIdAndCustomerUserUsername(
                        customerId,
                        username
                )
                .stream()
                .findAny()
                .isPresent();
    }
}