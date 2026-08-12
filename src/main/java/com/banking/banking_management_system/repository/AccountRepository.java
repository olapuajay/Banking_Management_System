package com.banking.banking_management_system.repository;

import com.banking.banking_management_system.entity.Account;
import com.banking.banking_management_system.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(Long customerId);
    boolean existsByAccountNumber(String accountNumber);
    List<Account> findByStatus(AccountStatus status);
}
