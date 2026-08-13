package com.bank.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, unique = true, length = 20)
    private String accountNumber;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)  //account ManyToOne customer
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY) //accountType ManyToOne account
    @JoinColumn(name = "account_type_id", nullable = false)
    private AccountType accountType;

    @Setter
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;  //initial balance 0.00

    @Setter
    @Enumerated(EnumType.STRING) //to store enum in the database
    @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Account() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}