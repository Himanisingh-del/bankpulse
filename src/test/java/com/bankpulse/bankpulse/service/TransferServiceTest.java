package com.bankpulse.bankpulse.service;

import com.bankpulse.bankpulse.dto.TransferRequest;
import com.bankpulse.bankpulse.entity.Account;
import com.bankpulse.bankpulse.repository.AccountRepository;
import com.bankpulse.bankpulse.entity.Transaction;
import com.bankpulse.bankpulse.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void transfer_shouldTransferMoneySuccessfully() {

        Account fromAccount = new Account(
                "Himani Singh",
                "1234567890",
                "himani@example.com",
                BigDecimal.valueOf(5000)
        );

        Account toAccount = new Account(
                "Rahul Sharma",
                "9876543210",
                "rahul@example.com",
                BigDecimal.valueOf(1000)
        );

        TransferRequest request = new TransferRequest();

        request.setFromAccountNumber("1234567890");
        request.setToAccountNumber("9876543210");
        request.setAmount(BigDecimal.valueOf(500));

        when(accountRepository.findByAccountNumber("1234567890"))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findByAccountNumber("9876543210"))
                .thenReturn(Optional.of(toAccount));

        transferService.transfer(request);

        assertEquals(
                BigDecimal.valueOf(4500),
                fromAccount.getBalance()
        );

        assertEquals(
                BigDecimal.valueOf(1500),
                toAccount.getBalance()
        );

        verify(accountRepository).save(fromAccount);
        verify(accountRepository).save(toAccount);

        verify(transactionRepository, times(2))
                .save(any(Transaction.class));
    }

    @Test
    void transfer_shouldRejectWhenInsufficientBalance() {

        Account fromAccount = new Account(
                "Himani Singh",
                "1234567890",
                "himani@example.com",
                BigDecimal.valueOf(500)
        );

        Account toAccount = new Account(
                "Rahul Sharma",
                "9876543210",
                "rahul@example.com",
                BigDecimal.valueOf(1000)
        );

        TransferRequest request = new TransferRequest();

        request.setFromAccountNumber("1234567890");
        request.setToAccountNumber("9876543210");
        request.setAmount(BigDecimal.valueOf(1000));

        when(accountRepository.findByAccountNumber("1234567890"))
                .thenReturn(Optional.of(fromAccount));

        when(accountRepository.findByAccountNumber("9876543210"))
                .thenReturn(Optional.of(toAccount));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> transferService.transfer(request)
                );

        assertEquals(
                "Insufficient balance",
                exception.getMessage()
        );

        assertEquals(
                BigDecimal.valueOf(500),
                fromAccount.getBalance()
        );

        assertEquals(
                BigDecimal.valueOf(1000),
                toAccount.getBalance()
        );

        verify(accountRepository, never())
                .save(any(Account.class));

        verify(transactionRepository, never())
                .save(any(Transaction.class));
    }
}