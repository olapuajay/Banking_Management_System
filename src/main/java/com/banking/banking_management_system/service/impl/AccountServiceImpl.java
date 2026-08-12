package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.request.account.CreateAccountRequest;
import com.banking.banking_management_system.dto.response.account.AccountResponse;
import com.banking.banking_management_system.entity.Account;
import com.banking.banking_management_system.entity.Customer;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.enums.AccountStatus;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.mapper.AccountMapper;
import com.banking.banking_management_system.repository.AccountRepository;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.repository.UserRepository;
import com.banking.banking_management_system.service.AccountService;
import com.banking.banking_management_system.util.AccountNumberGenerator;
import com.banking.banking_management_system.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final AccountNumberGenerator accountNumberGenerator;

    @Value("${bank.ifsc-code}")
    private String ifscCode;

    @Override
    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        String accountNumber;

        do {
            accountNumber = accountNumberGenerator.generate();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = new Account();

        account.setAccountNumber(accountNumber);
        account.setIfscCode(ifscCode);
        account.setAccountType(request.getAccountType());
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(AccountStatus.ACTIVE);
        account.setOpenedDate(LocalDate.now());
        account.setCustomer(customer);

        Account savedAccount = accountRepository.save(account);

        return accountMapper.toResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountByNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getMyAccounts() {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        return accountRepository
                .findByCustomerId(customer.getId())
                .stream()
                .map(accountMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AccountResponse freezeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setStatus(AccountStatus.FROZEN);

        return accountMapper.toResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse activateAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        account.setStatus(AccountStatus.ACTIVE);

        return accountMapper.toResponse(account);
    }
}
