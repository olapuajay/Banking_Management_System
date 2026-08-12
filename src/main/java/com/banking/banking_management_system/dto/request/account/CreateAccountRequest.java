package com.banking.banking_management_system.dto.request.account;

import com.banking.banking_management_system.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountRequest {
    @NotNull(message = "Account type is required")
    private AccountType accountType;
}
