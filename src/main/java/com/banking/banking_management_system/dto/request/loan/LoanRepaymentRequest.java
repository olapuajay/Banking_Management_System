package com.banking.banking_management_system.dto.request.loan;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRepaymentRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotNull(message = "Repayment amount is required")
    @DecimalMin(value = "0.01", message = "Repayment amount must be greater than zero")
    private BigDecimal amount;

    private String remarks;
}
