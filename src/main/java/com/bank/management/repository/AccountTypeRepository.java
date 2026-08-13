package com.bank.management.repository;

import com.bank.management.entity.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountTypeRepository
        extends JpaRepository<AccountType, Long> {

    Optional<AccountType> findByName(String name);
}