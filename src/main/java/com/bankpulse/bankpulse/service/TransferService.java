package com.bankpulse.bankpulse.service;

import com.bankpulse.bankpulse.dto.TransferRequest;
import com.bankpulse.bankpulse.entity.Account;
import com.bankpulse.bankpulse.entity.Transaction;
import com.bankpulse.bankpulse.repository.AccountRepository;
import com.bankpulse.bankpulse.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {

        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transfer(TransferRequest request) {

        // 1. Find source account
        Account fromAccount = accountRepository
                .findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException("Source account not found"));

        // 2. Find destination account
        Account toAccount = accountRepository
                .findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() ->
                        new RuntimeException("Destination account not found"));

        // 3. Prevent same account transfer
        if (fromAccount.getAccountNumber()
                .equals(toAccount.getAccountNumber())) {

            throw new RuntimeException(
                    "Source and destination accounts must be different");
        }

        // 4. Check sufficient balance
        if (fromAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new RuntimeException("Insufficient balance");
        }

        // 5. Debit source account
        fromAccount.setBalance(
                fromAccount.getBalance()
                        .subtract(request.getAmount())
        );

        // 6. Credit destination account
        toAccount.setBalance(
                toAccount.getBalance()
                        .add(request.getAmount())
        );

        // 7. Save updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // 8. Create transaction timestamp
        LocalDateTime now = LocalDateTime.now();

        // 9. Create DEBIT transaction
        Transaction debitTransaction = new Transaction(
                UUID.randomUUID().toString(),
                fromAccount.getAccountNumber(),
                "DEBIT",
                request.getAmount(),
                "Transfer to " + toAccount.getAccountNumber(),
                now
        );

        // 10. Create CREDIT transaction
        Transaction creditTransaction = new Transaction(
                UUID.randomUUID().toString(),
                toAccount.getAccountNumber(),
                "CREDIT",
                request.getAmount(),
                "Transfer from " + fromAccount.getAccountNumber(),
                now
        );

        // 11. Save transaction records
        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
    }
}