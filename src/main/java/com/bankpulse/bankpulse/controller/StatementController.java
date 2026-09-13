package com.bankpulse.bankpulse.controller;

import com.bankpulse.bankpulse.dto.AccountStatementResponse;
import com.bankpulse.bankpulse.service.StatementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class StatementController {

    private final StatementService statementService;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<AccountStatementResponse> getMonthlyStatement(
            @PathVariable String accountNumber,
            @RequestParam int month,
            @RequestParam int year) {

        AccountStatementResponse response =
                statementService.getMonthlyStatement(
                        accountNumber,
                        month,
                        year
                );

        return ResponseEntity.ok(response);
    }
}