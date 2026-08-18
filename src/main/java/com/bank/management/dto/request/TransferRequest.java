package com.bank.management.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

public class TransferRequest {

    @Getter
    @Setter
    private String senderAccountNumber;

    @Getter
    @Setter
    private String receiverAccountNumber;

    @Getter
    @Setter
    private BigDecimal amount;

    @Getter
    @Setter
    private String description;

    public TransferRequest() {
    }
}