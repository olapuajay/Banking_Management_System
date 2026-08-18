package com.banking.banking_management_system.dto.response.loan;

import com.banking.banking_management_system.enums.LoanStatus;
import com.banking.banking_management_system.enums.LoanType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
public class LoanResponse {
    private Long id;
    private String loanNumber;
    private LoanType loanType;
    private BigDecimal principalAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private BigDecimal emiAmount;
    private BigDecimal outstandingAmount;
    private LoanStatus status;
    private LocalDate applicationDate;
    private LocalDate approvalDate;
    private LocalDate maturityDate;
}
