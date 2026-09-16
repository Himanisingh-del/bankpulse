package com.bankpulse.bankpulse.service;

import com.bankpulse.bankpulse.dto.AccountMapper;
import com.bankpulse.bankpulse.dto.AccountRequest;
import com.bankpulse.bankpulse.dto.AccountResponse;
import com.bankpulse.bankpulse.entity.Account;
import com.bankpulse.bankpulse.repository.AccountRepository;
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
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountService accountService;

    @Test
    void createAccount_shouldCreateAccountSuccessfully() {

        AccountRequest request = new AccountRequest();

        request.setAccountHolderName("Himani Singh");
        request.setAccountNumber("1234567890");
        request.setEmail("himani@example.com");
        request.setBalance(BigDecimal.valueOf(5000));

        Account savedAccount = new Account(
                "Himani Singh",
                "1234567890",
                "himani@example.com",
                BigDecimal.valueOf(5000)
        );

        AccountResponse response = new AccountResponse(
                1L,
                "1234567890",
                "Himani Singh",
                "himani@example.com",
                BigDecimal.valueOf(5000)
        );

        when(accountRepository.findByAccountNumber("1234567890"))
                .thenReturn(Optional.empty());

        when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);

        when(accountMapper.toResponse(savedAccount))
                .thenReturn(response);

        AccountResponse result =
                accountService.createAccount(request);

        assertNotNull(result);
        assertEquals("1234567890", result.getAccountNumber());
        assertEquals("Himani Singh", result.getAccountHolderName());
        assertEquals(
                BigDecimal.valueOf(5000),
                result.getBalance()
        );

        verify(accountRepository).save(any(Account.class));
        verify(accountMapper).toResponse(savedAccount);
    }

    @Test
    void createAccount_shouldRejectDuplicateAccountNumber() {

        Account existingAccount = new Account(
                "Existing User",
                "1234567890",
                "existing@example.com",
                BigDecimal.valueOf(1000)
        );

        AccountRequest request = new AccountRequest();

        request.setAccountHolderName("Himani Singh");
        request.setAccountNumber("1234567890");
        request.setEmail("himani@example.com");
        request.setBalance(BigDecimal.valueOf(5000));

        when(accountRepository.findByAccountNumber("1234567890"))
                .thenReturn(Optional.of(existingAccount));

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> accountService.createAccount(request)
                );

        assertEquals(
                "Account number already exists",
                exception.getMessage()
        );

        verify(accountRepository, never())
                .save(any(Account.class));
    }
}