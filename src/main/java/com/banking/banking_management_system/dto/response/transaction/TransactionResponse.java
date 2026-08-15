package com.banking.banking_management_system.dto.response.transaction;

import com.banking.banking_management_system.enums.TransactionStatus;
import com.banking.banking_management_system.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class TransactionResponse {
    private Long id;
    private String referenceNumber;
    private BigDecimal amount;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private String remarks;
    private LocalDateTime createdAt;
}
