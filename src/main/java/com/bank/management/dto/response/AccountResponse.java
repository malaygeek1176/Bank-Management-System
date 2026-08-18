package com.bank.management.dto.response;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccountResponse {

    private Long id;

    private String accountNumber;

    private Long customerId;

    private String customerName;

    private Long accountTypeId;

    private String accountType;

    private BigDecimal balance;

    private String status;

    public AccountResponse() {
    }

    public AccountResponse(
            Long id,
            String accountNumber,
            Long customerId,
            String customerName,
            Long accountTypeId,
            String accountType,
            BigDecimal balance,
            String status) {

        this.id = id;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.accountTypeId = accountTypeId;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
    }

}