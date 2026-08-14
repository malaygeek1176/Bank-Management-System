package com.bank.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Getter
    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String transactionReference;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY) //one account multiple transactions
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY) // multiple deposit, withdraw
    @JoinColumn(name = "transaction_type_id", nullable = false)
    private TransactionType transactionType;

    @Getter
    @Setter
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Getter
    @Setter
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    @Getter
    @Setter
    @Column(length = 255)
    private String description;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status;

    @Getter
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Transaction() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}