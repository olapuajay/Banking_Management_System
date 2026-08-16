package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.request.transaction.DepositRequest;
import com.banking.banking_management_system.dto.request.transaction.TransferRequest;
import com.banking.banking_management_system.dto.request.transaction.WithdrawRequest;
import com.banking.banking_management_system.dto.response.transaction.TransactionResponse;
import com.banking.banking_management_system.entity.Account;
import com.banking.banking_management_system.entity.Customer;
import com.banking.banking_management_system.entity.Transaction;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.enums.AccountStatus;
import com.banking.banking_management_system.enums.TransactionStatus;
import com.banking.banking_management_system.enums.TransactionType;
import com.banking.banking_management_system.exception.*;
import com.banking.banking_management_system.mapper.TransactionMapper;
import com.banking.banking_management_system.repository.AccountRepository;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.repository.TransactionRepository;
import com.banking.banking_management_system.repository.UserRepository;
import com.banking.banking_management_system.service.TransactionService;
import com.banking.banking_management_system.util.SecurityUtils;
import com.banking.banking_management_system.util.TransactionReferenceGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionReferenceGenerator referenceGenerator;

    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        Account account = getCurrentCustomerAccountForUpdate(request.getAccountNumber());

        validateAccountForTransaction(account);

        account.setBalance(account.getBalance().add(request.getAmount()));

//        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setReferenceNumber(generateUniqueReference());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setRemarks(request.getRemarks());
        transaction.setDestinationAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        Account account = getCurrentCustomerAccountForUpdate(request.getAccountNumber());

        validateAccountForTransaction(account);

        if(account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient account balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

//        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setReferenceNumber(generateUniqueReference());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setRemarks(request.getRemarks());
        transaction.setSourceAccount(account);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        Account sourceAccount = accountRepository.findByAccountNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        if(!sourceAccount.getCustomer().getId().equals(customer.getId())) {
            throw new UnauthorizedException("You do not have access to the source account");
        }

        if (sourceAccount.getAccountNumber().equals(destinationAccount.getAccountNumber())) {
            throw new InvalidTransactionException("Source and destination accounts cannot be the same");
        }

        List<Long> accountIds = List.of(sourceAccount.getId(), destinationAccount.getId());

        accountIds = accountIds.stream()
                .sorted()
                .toList();

        List<Account> lockedAccounts = accountRepository.findAccountsForUpdate(accountIds);

        Account lockedSource = lockedAccounts.stream()
                .filter(a -> a.getId().equals(sourceAccount.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));

        Account lockedDestination = lockedAccounts.stream()
                .filter(a -> a.getId().equals(destinationAccount.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));

        validateAccountForTransaction(lockedSource);
        validateAccountForTransaction(lockedDestination);

        if(lockedSource.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient account balance");
        }

        lockedSource.setBalance(lockedSource.getBalance().subtract(request.getAmount()));
        lockedDestination.setBalance(lockedDestination.getBalance().add(request.getAmount()));

//        accountRepository.save(sourceAccount);
//        accountRepository.save(destinationAccount);

        Transaction transaction = new Transaction();

        transaction.setReferenceNumber(generateUniqueReference());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setRemarks(request.getRemarks());
        transaction.setSourceAccount(lockedSource);
        transaction.setDestinationAccount(lockedDestination);

        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toResponse(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getMyTransactionHistory(Pageable pageable) {
        String email = SecurityUtils.getCurrentUserEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        return transactionRepository
                .findCustomerTransactions(customer.getId(), pageable)
                .map(transactionMapper::toResponse);
    }

    private void validateAccountForTransaction(Account account) {
        if(account.getStatus() == AccountStatus.FROZEN) {
            throw new AccountFrozenException("Account is frozen");
        }

        if(account.getStatus() == AccountStatus.CLOSED) {
            throw new InvalidTransactionException("Account is closed");
        }
    }

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

    private Account getCurrentCustomerAccount() {
        throw new UnsupportedOperationException();
    }

    private String generateUniqueReference() {
        String reference;

        do {
            reference = referenceGenerator.generate();
        } while (transactionRepository.findByReferenceNumber(reference).isPresent());

        return reference;
    }

    private Account getCurrentCustomerAccountForUpdate(String accountNumber) {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Customer customer = customerRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        Account account = accountRepository.findByAccountNumberForUpdate(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if(!account.getCustomer().getId().equals(customer.getId())) {
            throw new UnauthorizedException("You do not have access to this account");
        }

        return account;
    }
}
