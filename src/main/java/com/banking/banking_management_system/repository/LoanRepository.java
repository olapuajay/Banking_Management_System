package com.banking.banking_management_system.repository;

import com.banking.banking_management_system.entity.Loan;
import com.banking.banking_management_system.enums.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByLoanNumber(String loanNumber);
    Page<Loan> findByCustomerId(Long customerId, Pageable pageable);
    Page<Loan> findByStatus(LoanStatus status, Pageable pageable);
    boolean existsByLoanNumber(String loanNumber);
}
