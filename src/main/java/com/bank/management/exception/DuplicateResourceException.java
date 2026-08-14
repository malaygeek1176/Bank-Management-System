package com.bank.management.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
//Email already registered
//        ↓
//DuplicateResourceException