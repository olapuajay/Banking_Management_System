package com.banking.banking_management_system.dto.response.statement;

import com.banking.banking_management_system.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class StatementTransactionResponse {
    private LocalDateTime date;
    private String referenceNumber;
    private TransactionType transactionType;
    private String description;
    private BigDecimal debit;
    private BigDecimal credit;
    private BigDecimal balance;
}
