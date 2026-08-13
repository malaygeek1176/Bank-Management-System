package com.bank.management.repository;

import com.bank.management.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionTypeRepository
        extends JpaRepository<TransactionType, Long> {

    Optional<TransactionType> findByName(String name);
}