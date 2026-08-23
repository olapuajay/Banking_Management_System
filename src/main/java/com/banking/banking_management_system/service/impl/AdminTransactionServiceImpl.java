package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.response.transaction.TransactionResponse;
import com.banking.banking_management_system.entity.Transaction;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.mapper.TransactionMapper;
import com.banking.banking_management_system.repository.TransactionRepository;
import com.banking.banking_management_system.service.AdminTransactionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminTransactionServiceImpl implements AdminTransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAllTransactions(Pageable pageable) {
        return transactionRepository
                .findAll(pageable)
                .map(transactionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(Long transactionId) {
        Transaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        return transactionMapper.toResponse(transaction);
    }
}
