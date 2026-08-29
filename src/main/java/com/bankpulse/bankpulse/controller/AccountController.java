package com.bankpulse.bankpulse.controller;

import com.bankpulse.bankpulse.dto.AccountRequest;
import com.bankpulse.bankpulse.dto.AccountResponse;
import com.bankpulse.bankpulse.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest request) {

        AccountResponse response = accountService.createAccount(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<AccountResponse> getAccountBalance(
            @PathVariable String accountNumber) {

        AccountResponse response =
                accountService.getAccountBalance(accountNumber);

        return ResponseEntity.ok(response);
    }
}