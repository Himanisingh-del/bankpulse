package com.bankpulse.bankpulse.controller;

import com.bankpulse.bankpulse.dto.AccountRequest;
import com.bankpulse.bankpulse.dto.AccountResponse;
import com.bankpulse.bankpulse.dto.TransferRequest;
import com.bankpulse.bankpulse.service.AccountService;
import com.bankpulse.bankpulse.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransferService transferService;

    public AccountController(
            AccountService accountService,
            TransferService transferService) {

        this.accountService = accountService;
        this.transferService = transferService;
    }

    // Create Account
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest request) {

        AccountResponse response =
                accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Balance Lookup
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<AccountResponse> getAccountBalance(
            @PathVariable String accountNumber) {

        AccountResponse response =
                accountService.getAccountBalance(accountNumber);

        return ResponseEntity.ok(response);
    }

    // Money Transfer
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
            @Valid @RequestBody TransferRequest request) {

        transferService.transfer(request);

        return ResponseEntity.ok("Transfer successful");
    }
}