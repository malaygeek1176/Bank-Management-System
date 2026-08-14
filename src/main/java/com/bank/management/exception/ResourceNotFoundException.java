package com.bank.management.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
//Customer ID: 10
//        ↓
//Database
//       ↓
//Not Found
//       ↓
//ResourceNotFoundException