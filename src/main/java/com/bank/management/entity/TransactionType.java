package com.bank.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transaction_types")
public class TransactionType {

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

    public TransactionType() {
    }

}