package com.bank.management.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;


public class WithdrawalRequest {

    @Getter
    @Setter
    private String accountNumber;

    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    private String description;

    public WithdrawalRequest() {
    }
}