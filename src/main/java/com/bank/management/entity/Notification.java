package com.bank.management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Getter
    @Setter
    @Column(nullable = false, length = 150)
    private String title;

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Getter
    @Setter
    @Column(nullable = false)
    private boolean isRead = false;

    @Getter
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Notification() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}