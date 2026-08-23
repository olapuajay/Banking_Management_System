package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.response.transaction.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminTransactionService {
    Page<TransactionResponse> getAllTransactions(Pageable pageable);

    TransactionResponse getTransaction(Long transactionId);
}
