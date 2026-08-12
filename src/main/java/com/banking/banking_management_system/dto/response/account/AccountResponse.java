package com.banking.banking_management_system.dto.response.account;

import com.banking.banking_management_system.enums.AccountStatus;
import com.banking.banking_management_system.enums.AccountType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String ifscCode;
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDate openedDate;
}
