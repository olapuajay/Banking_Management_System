package com.banking.banking_management_system.dto.request.loan;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoanDisbursementRequest {
    @NotBlank(message = "Account number is required")
    private String accountNumber;
}
