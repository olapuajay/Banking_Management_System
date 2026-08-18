package com.banking.banking_management_system.service;

import com.banking.banking_management_system.dto.request.loan.LoanApplicationRequest;
import com.banking.banking_management_system.dto.response.loan.LoanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    LoanResponse applyForLoan(LoanApplicationRequest request);
    Page<LoanResponse> getMyLoans(Pageable pageable);
    LoanResponse getLoan(Long loanId);
    LoanResponse approveLoan(Long loanId);
    LoanResponse rejectLoan(Long loanId);
}
