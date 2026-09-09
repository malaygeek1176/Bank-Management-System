package com.bank.management.repository;

import com.bank.management.entity.Account;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository
        extends JpaRepository<Account, Long> {

    // Find account by account number
    Optional<Account> findByAccountNumber(
            String accountNumber
    );

    // Find all accounts belonging to a customer
    List<Account> findByCustomerId(
            Long customerId
    );

    // Check whether account number already exists
    boolean existsByAccountNumber(
            String accountNumber
    );

    // PESSIMISTIC LOCKING
    // Used during money transfer to prevent
    // concurrent balance modification.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT a
            FROM Account a
            WHERE a.accountNumber = :accountNumber
            """)
    Optional<Account> findByAccountNumberForUpdate(
            @Param("accountNumber") String accountNumber
    );

    // ACCOUNT OWNERSHIP

    // Check whether an account belongs to
    // the logged-in user's customer.
    Optional<Account> findByIdAndCustomerUserUsername(
            Long id,
            String username
    );

    // Check ownership using account number.
    Optional<Account> findByAccountNumberAndCustomerUserUsername(
            String accountNumber,
            String username
    );

    // Find accounts of a customer only when
    // that customer belongs to the logged-in user.
    List<Account> findByCustomerIdAndCustomerUserUsername(
            Long customerId,
            String username
    );
}