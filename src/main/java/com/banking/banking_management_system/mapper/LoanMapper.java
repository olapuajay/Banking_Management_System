package com.banking.banking_management_system.mapper;

import com.banking.banking_management_system.dto.response.loan.LoanResponse;
import com.banking.banking_management_system.entity.Loan;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
    public LoanResponse toResponse(Loan loan) {
        return LoanResponse.builder()
                .id(loan.getId())
                .loanNumber(loan.getLoanNumber())
                .loanType(loan.getLoanType())
                .principalAmount(loan.getPrincipalAmount())
                .interestRate(loan.getInterestRate())
                .tenureMonths(loan.getTenureMonths())
                .emiAmount(loan.getEmiAmount())
                .outstandingAmount(loan.getOutstandingAmount())
                .status(loan.getStatus())
                .applicationDate(loan.getApplicationDate())
                .approvalDate(loan.getApprovalDate())
                .maturityDate(loan.getMaturityDate())
                .build();
    }
}
