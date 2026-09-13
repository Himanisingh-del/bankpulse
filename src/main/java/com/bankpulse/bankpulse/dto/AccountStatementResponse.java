package com.bankpulse.bankpulse.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccountStatementResponse {

    private String accountNumber;
    private String accountHolderName;
    private BigDecimal currentBalance;
    private String month;
    private List<TransactionResponse> transactions;

    public AccountStatementResponse() {
    }

    public AccountStatementResponse(
            String accountNumber,
            String accountHolderName,
            BigDecimal currentBalance,
            String month,
            List<TransactionResponse> transactions) {

        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.currentBalance = currentBalance;
        this.month = month;
        this.transactions = transactions;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public String getMonth() {
        return month;
    }

    public List<TransactionResponse> getTransactions() {
        return transactions;
    }
}