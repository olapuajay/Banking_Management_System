package com.banking.banking_management_system.controller;

import com.banking.banking_management_system.dto.response.statement.StatementResponse;
import com.banking.banking_management_system.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statements")
@RequiredArgsConstructor
public class StatementController {
    private final StatementService statementService;

    @GetMapping("/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<StatementResponse> generateStatement(
            @PathVariable String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(
                statementService.generateStatement(accountNumber, from, to)
        );
    }
}
