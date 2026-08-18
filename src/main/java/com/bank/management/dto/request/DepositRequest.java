package com.bank.management.dto.request;

import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

public class DepositRequest {

    @Getter
    @Setter
    private String accountNumber;

    @Setter
    @Getter
    private BigDecimal amount;

    @Getter
    @Setter
    private String description;

    public DepositRequest() {
    }

}