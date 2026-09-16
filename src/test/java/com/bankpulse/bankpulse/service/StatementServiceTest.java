package com.bankpulse.bankpulse.service;

import com.bankpulse.bankpulse.dto.AccountStatementResponse;
import com.bankpulse.bankpulse.dto.TransactionMapper;
import com.bankpulse.bankpulse.dto.TransactionResponse;
import com.bankpulse.bankpulse.entity.Account;
import com.bankpulse.bankpulse.entity.Transaction;
import com.bankpulse.bankpulse.repository.AccountRepository;
import com.bankpulse.bankpulse.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private StatementService statementService;

    @Test
    void getMonthlyStatement_shouldReturnStatementSuccessfully() {

        Account account = new Account(
                "Himani Singh",
                "1234567890",
                "himani@example.com",
                BigDecimal.valueOf(4500)
        );

        Transaction transaction = new Transaction(
                "TXN-001",
                "1234567890",
                "DEBIT",
                BigDecimal.valueOf(500),
                "Transfer to 9876543210",
                LocalDateTime.of(2026, 9, 15, 10, 30)
        );

        TransactionResponse transactionResponse =
                new TransactionResponse(
                        "TXN-001",
                        "1234567890",
                        "DEBIT",
                        BigDecimal.valueOf(500),
                        "Transfer to 9876543210",
                        transaction.getCreatedAt()
                );

        when(accountRepository.findByAccountNumber("1234567890"))
                .thenReturn(Optional.of(account));

        when(transactionRepository
                .findByAccountNumberAndCreatedAtBetween(
                        eq("1234567890"),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                ))
                .thenReturn(List.of(transaction));

        when(transactionMapper.toResponse(transaction))
                .thenReturn(transactionResponse);

        AccountStatementResponse result =
                statementService.getMonthlyStatement(
                        "1234567890",
                        9,
                        2026
                );

        assertNotNull(result);

        assertEquals(
                "1234567890",
                result.getAccountNumber()
        );

        assertEquals(
                "Himani Singh",
                result.getAccountHolderName()
        );

        assertEquals(
                BigDecimal.valueOf(4500),
                result.getCurrentBalance()
        );

        assertEquals(
                "SEPTEMBER 2026",
                result.getMonth()
        );

        assertEquals(
                1,
                result.getTransactions().size()
        );

        assertEquals(
                "TXN-001",
                result.getTransactions().get(0).getTransactionId()
        );

        verify(transactionMapper)
                .toResponse(transaction);
    }

    @Test
    void getMonthlyStatement_shouldRejectInvalidMonth() {

        Account account = new Account(
                "Himani Singh",
                "1234567890",
                "himani@example.com",
                BigDecimal.valueOf(4500)
        );

        when(accountRepository.findByAccountNumber("1234567890"))
                .thenReturn(Optional.of(account));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> statementService.getMonthlyStatement(
                                "1234567890",
                                13,
                                2026
                        )
                );

        assertEquals(
                "Month must be between 1 and 12",
                exception.getMessage()
        );

        verify(transactionRepository, never())
                .findByAccountNumberAndCreatedAtBetween(
                        anyString(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class)
                );
    }
}