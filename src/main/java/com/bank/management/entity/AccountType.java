package com.bank.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "account_types")
public class AccountType {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Getter
    @Setter
    @Column(columnDefinition = "TEXT")
    private String description;

    @Getter
    @Setter
    @Column(nullable = false, precision = 15, scale = 2) //precision = total digits, scale = decimal ke baad
    private BigDecimal minimumBalance;

    public AccountType() {
    }

}