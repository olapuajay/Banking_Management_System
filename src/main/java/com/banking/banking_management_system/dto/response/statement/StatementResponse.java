package com.banking.banking_management_system.dto.response.statement;

import com.banking.banking_management_system.enums.AccountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class StatementResponse {
    private String accountNumber;
    private AccountType accountType;
    private String customerName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal openingBalance;
    private BigDecimal closingBalance;
    private List<StatementTransactionResponse> transactions;
}
