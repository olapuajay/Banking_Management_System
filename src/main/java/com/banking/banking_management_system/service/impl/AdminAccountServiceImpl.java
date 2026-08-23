package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.response.account.AccountResponse;
import com.banking.banking_management_system.entity.Account;
import com.banking.banking_management_system.enums.AccountStatus;
import com.banking.banking_management_system.exception.InvalidTransactionException;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.mapper.AccountMapper;
import com.banking.banking_management_system.repository.AccountRepository;
import com.banking.banking_management_system.service.AdminAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AdminAccountServiceImpl implements AdminAccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<AccountResponse> getAllAccounts(Pageable pageable) {
        return accountRepository
                .findAll(pageable)
                .map(accountMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long accountId) {
        Account account = getAccountEntity(accountId);

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse freezeAccount(Long accountId) {
        Account account = getAccountEntity(accountId);

        if(account.getStatus() == AccountStatus.CLOSED) {
            throw new InvalidTransactionException("Closed account cannot be frozen");
        }

        account.setStatus(AccountStatus.FROZEN);

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse unfreezeAccount(Long accountId) {
        Account account = getAccountEntity(accountId);

        if(account.getStatus() != AccountStatus.FROZEN) {
            throw new InvalidTransactionException("Only frozen accounts can be unfrozen");
        }

        account.setStatus(AccountStatus.ACTIVE);

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse closeAccount(Long accountId) {
        Account account = getAccountEntity(accountId);

        if(account.getStatus() == AccountStatus.CLOSED) {
            throw new InvalidTransactionException("Account is already closed");
        }

        if(account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
            throw new InvalidTransactionException("Account balance must be zero before closing");
        }

        account.setStatus(AccountStatus.CLOSED);

        return accountMapper.toResponse(account);
    }

    private Account getAccountEntity(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
    }
}
