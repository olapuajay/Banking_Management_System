package com.banking.banking_management_system.repository;

import com.banking.banking_management_system.entity.Transaction;
import com.banking.banking_management_system.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReferenceNumber(String referenceNumber);
    Page<Transaction> findBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId, Pageable pageable);
    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);
}
