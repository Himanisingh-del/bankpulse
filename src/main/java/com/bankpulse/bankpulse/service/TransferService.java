package com.bankpulse.bankpulse.service;

import com.bankpulse.bankpulse.dto.TransferRequest;
import com.bankpulse.bankpulse.entity.Account;
import com.bankpulse.bankpulse.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

    private final AccountRepository accountRepository;

    public TransferService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transfer(TransferRequest request) {

        // Find sender account
        Account fromAccount = accountRepository
                .findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException("Source account not found"));

        // Find receiver account
        Account toAccount = accountRepository
                .findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException("Destination account not found"));

        // Prevent transferring to the same account
        if (fromAccount.getAccountNumber()
                .equals(toAccount.getAccountNumber())) {

            throw new RuntimeException(
                    "Source and destination accounts must be different");
        }

        // Check sufficient balance
        if (fromAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new RuntimeException("Insufficient balance");
        }

        // Deduct money from sender
        fromAccount.setBalance(
                fromAccount.getBalance()
                        .subtract(request.getAmount())
        );

        // Add money to receiver
        toAccount.setBalance(
                toAccount.getBalance()
                        .add(request.getAmount())
        );

        // Save both accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}