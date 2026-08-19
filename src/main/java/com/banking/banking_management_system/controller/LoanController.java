package com.banking.banking_management_system.controller;

import com.banking.banking_management_system.dto.request.loan.LoanApplicationRequest;
import com.banking.banking_management_system.dto.request.loan.LoanDisbursementRequest;
import com.banking.banking_management_system.dto.request.loan.LoanRepaymentRequest;
import com.banking.banking_management_system.dto.response.loan.LoanResponse;
import com.banking.banking_management_system.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<LoanResponse> applyForLoan(@Valid @RequestBody LoanApplicationRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(loanService.applyForLoan(request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Page<LoanResponse>> getMyLoans(Pageable pageable) {
        return ResponseEntity.ok(loanService.getMyLoans(pageable));
    }

    @GetMapping("/{loanId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<LoanResponse> getLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.getLoan(loanId));
    }

    @PutMapping("/{loanId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> approveLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.approveLoan(loanId));
    }

    @PutMapping("/{loanId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> rejectLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(loanService.rejectLoan(loanId));
    }

    @PutMapping("/{loanId}/disburse")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LoanResponse> disburseLoan(@PathVariable Long loanId, @Valid @RequestBody LoanDisbursementRequest request) {
        return ResponseEntity.ok(loanService.disburseLoan(loanId, request));
    }

    @PostMapping("/{loanId}/repay")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<LoanResponse> repayLoan(@PathVariable Long loanId, @Valid @RequestBody LoanRepaymentRequest request) {
        return ResponseEntity.ok(loanService.repayLoan(loanId, request));
    }
}
