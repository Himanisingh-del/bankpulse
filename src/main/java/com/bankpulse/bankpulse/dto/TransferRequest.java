package com.bankpulse.bankpulse.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class TransferRequest {

    @NotNull(message = "Source account number is required")
    @Size(min = 10, max = 20, message = "Source account number must be between 10 and 20 characters")
    private String fromAccountNumber;

    @NotNull(message = "Destination account number is required")
    @Size(min = 10, max = 20, message = "Destination account number must be between 10 and 20 characters")
    private String toAccountNumber;

    @DecimalMin(value = "0.01", message = "Transfer amount must be greater than zero")
    @NotNull(message = "Transfer amount is required")
    private BigDecimal amount;

    public TransferRequest() {
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}