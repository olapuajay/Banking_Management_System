package com.banking.banking_management_system.dto.response.admin;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class AdminDashboardResponse {
    private long totalCustomers;
    private long totalAccounts;
    private long activeAccounts;
    private long frozenAccounts;
    private long closedAccounts;
    private long totalTransactions;
    private BigDecimal totalTransactionAmount;
    private long pendingLoans;
    private long activeLoans;
    private long completedLoans;
    private long rejectedLoans;
}
