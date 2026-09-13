package com.bankpulse.bankpulse.service;

import com.bankpulse.bankpulse.dto.AccountStatementResponse;
import com.bankpulse.bankpulse.dto.TransactionMapper;
import com.bankpulse.bankpulse.dto.TransactionResponse;
import com.bankpulse.bankpulse.entity.Account;
import com.bankpulse.bankpulse.entity.Transaction;
import com.bankpulse.bankpulse.repository.AccountRepository;
import com.bankpulse.bankpulse.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Service
public class StatementService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public StatementService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository,
            TransactionMapper transactionMapper) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public AccountStatementResponse getMonthlyStatement(
            String accountNumber,
            int month,
            int year) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber
                        )
                );

        if (month < 1 || month > 12) {
            throw new RuntimeException(
                    "Month must be between 1 and 12"
            );
        }

        LocalDateTime startDate = LocalDateTime.of(
                year,
                Month.of(month),
                1,
                0,
                0,
                0
        );

        LocalDateTime endDate = startDate.plusMonths(1);

        List<Transaction> transactions =
                transactionRepository
                        .findByAccountNumberAndCreatedAtBetween(
                                accountNumber,
                                startDate,
                                endDate
                        );

        List<TransactionResponse> transactionResponses =
                transactions.stream()
                        .map(transactionMapper::toResponse)
                        .toList();

        String monthName =
                Month.of(month).name() + " " + year;

        return new AccountStatementResponse(
                account.getAccountNumber(),
                account.getAccountHolderName(),
                account.getBalance(),
                monthName,
                transactionResponses
        );
    }
}