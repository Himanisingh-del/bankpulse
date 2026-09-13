package com.bankpulse.bankpulse.service;

import com.bankpulse.bankpulse.dto.AccountMapper;
import com.bankpulse.bankpulse.dto.AccountRequest;
import com.bankpulse.bankpulse.dto.AccountResponse;
import com.bankpulse.bankpulse.entity.Account;
import com.bankpulse.bankpulse.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(
            AccountRepository accountRepository,
            AccountMapper accountMapper) {

        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public AccountResponse createAccount(AccountRequest request) {

        if (accountRepository
                .findByAccountNumber(request.getAccountNumber())
                .isPresent()) {

            throw new RuntimeException("Account number already exists");
        }

        Account account = new Account(
                request.getAccountHolderName(),
                request.getAccountNumber(),
                request.getEmail(),
                request.getBalance()
        );

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    public AccountResponse getAccountBalance(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber
                        )
                );

        return accountMapper.toResponse(account);
    }
}