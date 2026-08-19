package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.config.LoanInterestRateConfig;
import com.banking.banking_management_system.dto.request.loan.LoanApplicationRequest;
import com.banking.banking_management_system.dto.request.loan.LoanDisbursementRequest;
import com.banking.banking_management_system.dto.request.loan.LoanRepaymentRequest;
import com.banking.banking_management_system.dto.response.loan.LoanResponse;
import com.banking.banking_management_system.entity.*;
import com.banking.banking_management_system.enums.AccountStatus;
import com.banking.banking_management_system.enums.LoanStatus;
import com.banking.banking_management_system.enums.TransactionStatus;
import com.banking.banking_management_system.enums.TransactionType;
import com.banking.banking_management_system.exception.*;
import com.banking.banking_management_system.mapper.LoanMapper;
import com.banking.banking_management_system.repository.*;
import com.banking.banking_management_system.service.LoanCalculationService;
import com.banking.banking_management_system.service.LoanService;
import com.banking.banking_management_system.util.LoanNumberGenerator;
import com.banking.banking_management_system.util.SecurityUtils;
import com.banking.banking_management_system.util.TransactionReferenceGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    private final LoanMapper loanMapper;
    private final LoanNumberGenerator loanNumberGenerator;
    private final LoanCalculationService loanCalculationService;
    private final LoanInterestRateConfig interestRateConfig;

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionReferenceGenerator transactionReferenceGenerator;

    @Override
    @Transactional
    public LoanResponse applyForLoan(LoanApplicationRequest request) {
        Customer customer = getCurrentCustomer();

        BigDecimal interestRate = interestRateConfig.getRate(request.getLoanType());
        BigDecimal emi = loanCalculationService.calculateEmi(request.getPrincipalAmount(), interestRate, request.getTenureMonths());

        Loan loan = new Loan();

        loan.setLoanNumber(generateUniqueLoanNumber());
        loan.setLoanType(request.getLoanType());
        loan.setPrincipalAmount(request.getPrincipalAmount());
        loan.setInterestRate(interestRate);
        loan.setTenureMonths(request.getTenureMonths());
        loan.setEmiAmount(emi);
        loan.setOutstandingAmount(request.getPrincipalAmount());
        loan.setStatus(LoanStatus.PENDING);
        loan.setApplicationDate(LocalDate.now());
        loan.setCustomer(customer);

        Loan savedLoan = loanRepository.save(loan);

        return loanMapper.toResponse(savedLoan);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LoanResponse> getMyLoans(Pageable pageable) {
        Customer customer = getCurrentCustomer();

        return loanRepository
                .findByCustomerId(customer.getId(), pageable)
                .map(loanMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LoanResponse getLoan(Long loanId) {
        Customer customer = getCurrentCustomer();

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if(!loan.getCustomer().getId().equals(customer.getId())) {
            throw new ResourceNotFoundException("Loan not found");
        }

        return loanMapper.toResponse(loan);
    }

    @Override
    @Transactional
    public LoanResponse approveLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if(loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidTransactionException("Only pending loans can be approved");
        }

        loan.setStatus(LoanStatus.APPROVED);
        loan.setApprovalDate(LocalDate.now());
        loan.setMaturityDate(LocalDate.now().plusMonths(loan.getTenureMonths()));

        return loanMapper.toResponse(loan);
    }

    @Override
    @Transactional
    public LoanResponse rejectLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if(loan.getStatus() != LoanStatus.PENDING) {
            throw new InvalidTransactionException("Only pending loans can be rejected");
        }

        loan.setStatus(LoanStatus.REJECTED);

        return loanMapper.toResponse(loan);
    }

    @Override
    @Transactional
    public LoanResponse disburseLoan(Long loanId, LoanDisbursementRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if(loan.getStatus() != LoanStatus.APPROVED) {
            throw new InvalidTransactionException("Only approved loans can be disbursed");
        }

        Account account = accountRepository.findByAccountNumberForUpdate(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if(!account.getCustomer().getId().equals(loan.getCustomer().getId())) {
            throw new UnauthorizedException("Account does not belong to loan customer");
        }

        validateAccountForLoanTransaction(account);

        account.setBalance(account.getBalance().add(loan.getPrincipalAmount()));

        Transaction transaction = new Transaction();

        transaction.setReferenceNumber(generateUniqueTransactionReference());
        transaction.setAmount(loan.getPrincipalAmount());
        transaction.setTransactionType(TransactionType.LOAN_DISBURSEMENT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setRemarks("Loan disbursement: " + loan.getLoanNumber());
        transaction.setDestinationAccount(account);
        transactionRepository.save(transaction);

        loan.setStatus(LoanStatus.ACTIVE);

        return loanMapper.toResponse(loan);
    }

    @Override
    @Transactional
    public LoanResponse repayLoan(Long loanId, LoanRepaymentRequest request) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));

        if(loan.getStatus() != LoanStatus.ACTIVE) {
            throw new InvalidTransactionException("Only active loans can be repaid");
        }

        if(request.getAmount().compareTo(loan.getOutstandingAmount()) > 0) {
            throw new InvalidTransactionException("Repayment amount exceeds the outstanding loan amount");
        }

        Account account = accountRepository.findByAccountNumberForUpdate(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if(!account.getCustomer().getId().equals(loan.getCustomer().getId())) {
            throw new UnauthorizedException("Account does not belong to loan customer");
        }

        validateAccountForLoanTransaction(account);

        if(account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient account balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        loan.setOutstandingAmount(loan.getOutstandingAmount().subtract(request.getAmount()));

        if(loan.getOutstandingAmount().compareTo(BigDecimal.ZERO) == 0) {
            loan.setStatus(LoanStatus.COMPLETED);
        }

        Transaction transaction = new Transaction();

        transaction.setReferenceNumber(generateUniqueTransactionReference());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionType(TransactionType.LOAN_REPAYMENT);
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setRemarks(request.getRemarks());
        transaction.setSourceAccount(account);

        transactionRepository.save(transaction);

        return loanMapper.toResponse(loan);
    }

    private Customer getCurrentCustomer() {
        String email = SecurityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return customerRepository
                .findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));
    }

    private String generateUniqueLoanNumber() {
        String loanNumber;

        do {
            loanNumber = loanNumberGenerator.generate();
        } while (loanRepository.existsByLoanNumber(loanNumber));

        return loanNumber;
    }

    private void validateAccountForLoanTransaction(Account account) {
        if(account.getStatus() == AccountStatus.FROZEN) {
            throw new AccountFrozenException("Account is frozen");
        }

        if(account.getStatus() == AccountStatus.CLOSED) {
            throw new InvalidTransactionException("Account is closed");
        }
    }

    private String generateUniqueTransactionReference() {
        String reference;

        do {
            reference = transactionReferenceGenerator.generate();
        } while (transactionRepository.existsByReferenceNumber(reference));

        return reference;
    }
}
