package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.response.account.AccountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminAccountService {
    Page<AccountResponse> getAllAccounts(Pageable pageable);

    AccountResponse getAccount(Long accountId);

    AccountResponse freezeAccount(Long accountId);

    AccountResponse unfreezeAccount(Long accountId);

    AccountResponse closeAccount(Long accountId);
}
