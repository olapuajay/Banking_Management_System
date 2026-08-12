package com.banking.banking_management_system.mapper;

import com.banking.banking_management_system.dto.response.account.AccountResponse;
import com.banking.banking_management_system.entity.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .ifscCode(account.getIfscCode())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .openedDate(account.getOpenedDate())
                .build();
    }
}
