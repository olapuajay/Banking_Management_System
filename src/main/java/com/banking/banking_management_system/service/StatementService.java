package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.response.statement.StatementResponse;

import java.time.LocalDate;

public interface StatementService {
    StatementResponse generateStatement(String accountNumber, LocalDate from, LocalDate to);
}
