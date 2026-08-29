package com.bankpulse.bankpulse.service;

import com.bankpulse.bankpulse.dto.AccountRequest;
import com.bankpulse.bankpulse.dto.AccountResponse;
import com.bankpulse.bankpulse.entity.Account;
import com.bankpulse.bankpulse.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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

        return convertToResponse(savedAccount);
    }

    public AccountResponse getAccountBalance(String accountNumber) {

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber
                        )
                );

        return convertToResponse(account);
    }

    private AccountResponse convertToResponse(Account account) {

        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountHolderName(),
                account.getEmail(),
                account.getBalance()
        );
    }
}