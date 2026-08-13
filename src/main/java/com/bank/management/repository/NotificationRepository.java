package com.bank.management.repository;

import com.bank.management.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId);

    List<Notification> findByCustomerIdAndIsReadFalse(
            Long customerId);
}