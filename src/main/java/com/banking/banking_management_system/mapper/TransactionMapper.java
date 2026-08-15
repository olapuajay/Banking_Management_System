package com.banking.banking_management_system.mapper;

import com.banking.banking_management_system.dto.response.transaction.TransactionResponse;
import com.banking.banking_management_system.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .referenceNumber(transaction.getReferenceNumber())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .status(transaction.getStatus())
                .sourceAccountNumber(transaction.getSourceAccount() != null ? transaction.getSourceAccount().getAccountNumber() : null)
                .destinationAccountNumber(transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getAccountNumber() : null)
                .remarks(transaction.getRemarks())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
