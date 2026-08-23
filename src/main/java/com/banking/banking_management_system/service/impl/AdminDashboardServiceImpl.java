package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.response.admin.AdminDashboardResponse;
import com.banking.banking_management_system.enums.AccountStatus;
import com.banking.banking_management_system.enums.LoanStatus;
import com.banking.banking_management_system.repository.AccountRepository;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.repository.LoanRepository;
import com.banking.banking_management_system.repository.TransactionRepository;
import com.banking.banking_management_system.service.AdminDashboardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboard() {
        long totalCustomers = customerRepository.count();

        long totalAccounts = accountRepository.count();

        long activeAccounts = accountRepository.countByStatus(AccountStatus.ACTIVE);

        long frozenAccounts = accountRepository.countByStatus(AccountStatus.FROZEN);

        long closedAccounts = accountRepository.countByStatus(AccountStatus.CLOSED);

        long totalTransactions = transactionRepository.count();

        BigDecimal totalTransactionAmount = transactionRepository.getTotalSuccessfulTransactionAmount();

        long pendingLoans = loanRepository.countByStatus(LoanStatus.PENDING);

        long activeLoans = loanRepository.countByStatus(LoanStatus.ACTIVE);

        long completedLoans = loanRepository.countByStatus(LoanStatus.COMPLETED);

        long rejectedLoans = loanRepository.countByStatus(LoanStatus.REJECTED);

        return AdminDashboardResponse.builder()
                .totalCustomers(totalCustomers)
                .totalAccounts(totalAccounts)
                .activeAccounts(activeAccounts)
                .frozenAccounts(frozenAccounts)
                .closedAccounts(closedAccounts)
                .totalTransactions(totalTransactions)
                .totalTransactionAmount(totalTransactionAmount)
                .pendingLoans(pendingLoans)
                .activeLoans(activeLoans)
                .completedLoans(completedLoans)
                .rejectedLoans(rejectedLoans)
                .build();
    }
}
