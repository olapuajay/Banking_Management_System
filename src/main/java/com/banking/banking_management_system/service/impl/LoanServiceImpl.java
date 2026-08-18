package com.banking.banking_management_system.service.impl;

import com.banking.banking_management_system.config.LoanInterestRateConfig;
import com.banking.banking_management_system.dto.request.loan.LoanApplicationRequest;
import com.banking.banking_management_system.dto.response.loan.LoanResponse;
import com.banking.banking_management_system.entity.Customer;
import com.banking.banking_management_system.entity.Loan;
import com.banking.banking_management_system.entity.User;
import com.banking.banking_management_system.enums.LoanStatus;
import com.banking.banking_management_system.exception.InvalidTransactionException;
import com.banking.banking_management_system.exception.ResourceNotFoundException;
import com.banking.banking_management_system.mapper.LoanMapper;
import com.banking.banking_management_system.repository.CustomerRepository;
import com.banking.banking_management_system.repository.LoanRepository;
import com.banking.banking_management_system.repository.UserRepository;
import com.banking.banking_management_system.service.LoanCalculationService;
import com.banking.banking_management_system.service.LoanService;
import com.banking.banking_management_system.util.LoanNumberGenerator;
import com.banking.banking_management_system.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

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

        loan.setStatus(LoanStatus.ACTIVE);
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
}
