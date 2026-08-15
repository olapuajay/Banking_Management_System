package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.entity.Account;
import com.banking.banking_management_system.entity.Customer;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.exception.UnauthorizedException;
import com.banking.banking_management_system.repository.AccountRepository;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.repository.UserRepository;
import com.banking.banking_management_system.util.SecurityUtils;

public class TransactionServiceImpl {
    private UserRepository userRepository;
    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;

    private Account getCurrentCustomerAccount(String accountNumber) {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if(!account.getCustomer().getId().equals(customer.getId())) {
            throw new UnauthorizedException("You do not have access to this account");
        }

        return account;
    }
}
