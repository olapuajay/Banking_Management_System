package com.banking.banking_management_system.dto.request.beneficiary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBeneficiaryRequest {
    @NotBlank(message = "Beneficiary name is required")
    @Size(max = 100, message = "Beneficiary name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^\\d{16}$", message = "Account number must contain 16 digits")
    private String accountNumber;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code")
    private String ifscCode;
}
