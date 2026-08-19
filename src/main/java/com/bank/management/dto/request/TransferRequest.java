package com.bank.management.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TransferRequest {

    @Getter
    @Setter
    @NotBlank(message = "Sender account number is required")
    private String senderAccountNumber;

    @Getter
    @Setter
    @NotBlank(message = "Receiver account number is required")
    private String receiverAccountNumber;

    @Getter
    @Setter
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Getter
    @Setter
    private String description;

    public TransferRequest() {
    }
}