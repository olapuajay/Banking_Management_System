package com.banking.banking_management_system.repository;

import com.banking.banking_management_system.entity.Transaction;
import com.banking.banking_management_system.enums.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByReferenceNumber(String referenceNumber);
    Page<Transaction> findBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId, Pageable pageable);
    Page<Transaction> findByStatus(TransactionStatus status, Pageable pageable);
    boolean existsByReferenceNumber(String referenceNumber);
    @Query("""
SELECT t FROM Transaction t WHERE t.sourceAccount.customer.id = :customerId OR t.destinationAccount.customer.id = :customerId ORDER BY t.createdAt DESC
""")
    Page<Transaction> findCustomerTransactions(@Param("customerId") Long customerId, Pageable pageable);
}
