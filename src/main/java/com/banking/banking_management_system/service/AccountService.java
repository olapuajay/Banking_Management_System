package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.request.account.CreateAccountRequest;
import com.banking.banking_management_system.dto.response.account.AccountResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);

    AccountResponse getAccountById(Long accountId);

    AccountResponse getAccountByNumber(String accountNumber);

    List<AccountResponse> getMyAccounts();

    AccountResponse freezeAccount(Long accountId);

    AccountResponse activateAccount(Long accountId);
}
