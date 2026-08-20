package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.dto.response.statement.StatementResponse;
import com.banking.banking_management_system.dto.response.statement.StatementTransactionResponse;
import com.banking.banking_management_system.entity.Account;
import com.banking.banking_management_system.entity.Transaction;
import com.banking.banking_management_system.exception.InvalidTransactionException;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.repository.AccountRepository;
import com.banking.banking_management_system.repository.TransactionRepository;
import com.banking.banking_management_system.service.StatementService;
import com.banking.banking_management_system.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementServiceImpl implements StatementService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional(readOnly = true)
    public StatementResponse generateStatement(String accountNumber, LocalDate from, LocalDate to) {
        validateDates(from, to);

        Account account = getAuthorizedAccount(accountNumber);

        LocalDateTime fromDateTime = from.atStartOfDay();
        LocalDateTime toDateTime = to.plusDays(1).atStartOfDay();

        List<Transaction> previousTransactions = transactionRepository.findTransactionsBefore(accountNumber, fromDateTime);

        BigDecimal openingBalance = calculateOpeningBalance(account, previousTransactions);

        List<Transaction> transactions = transactionRepository.findTransactionsForStatement(accountNumber, fromDateTime, toDateTime);

        BigDecimal runningBalance = openingBalance;

        List<StatementTransactionResponse> statementTransactions = new ArrayList<>();

        for(Transaction transaction: transactions) {
            BigDecimal debit = BigDecimal.ZERO;
            BigDecimal credit = BigDecimal.ZERO;

            if(isCredit(transaction, account)) {
                credit = transaction.getAmount();
                runningBalance = runningBalance.add(credit);
            } else if(isDebit(transaction, account)) {
                debit = transaction.getAmount();
                runningBalance = runningBalance.subtract(debit);
            }

            statementTransactions.add(
                    StatementTransactionResponse
                            .builder()
                            .date(transaction.getCreatedAt())
                            .referenceNumber(transaction.getReferenceNumber())
                            .transactionType(transaction.getTransactionType())
                            .description(transaction.getRemarks())
                            .debit(debit)
                            .credit(credit)
                            .balance(runningBalance)
                            .build()
            );
        }

        return StatementResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .customerName(account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName())
                .fromDate(from)
                .toDate(to)
                .openingBalance(openingBalance)
                .closingBalance(runningBalance)
                .transactions(statementTransactions)
                .build();
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if(from.isAfter(to)) {
            throw new InvalidTransactionException("From date cannot be after to date");
        }

        if(to.isAfter(LocalDate.now())) {
            throw new InvalidTransactionException("Statement cannot be generated for a future date");
        }
    }

    private Account getAuthorizedAccount(String accountNumber) {
        String email = SecurityUtils.getCurrentUserEmail();

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if(!account.getCustomer().getUser().getEmail().equals(email)) {
            throw new ResourceNotFoundException("Account not found");
        }

        return account;
    }

    private BigDecimal calculateOpeningBalance(
            Account account,
            List<Transaction> transactions
    ) {
        BigDecimal balance = account.getBalance();

        for (Transaction transaction : transactions) {
            if (isCredit(transaction, account)) {
                balance = balance.subtract(transaction.getAmount());
            } else if (isDebit(transaction, account)) {
                balance = balance.add(transaction.getAmount());
            }
        }

        return balance;
    }

    private boolean isCredit(Transaction transaction, Account account) {
        return transaction.getDestinationAccount() != null
                && transaction
                .getDestinationAccount()
                .getId()
                .equals(account.getId());
    }

    private boolean isDebit(Transaction transaction, Account account) {
        return transaction.getSourceAccount() != null
                && transaction
                .getSourceAccount()
                .getId()
                .equals(account.getId());
    }
}
