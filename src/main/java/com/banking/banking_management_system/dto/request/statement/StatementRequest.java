package com.banking.banking_management_system.dto.request.statement;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StatementRequest {
    @NotNull(message = "From date is required")
    private LocalDate from;

    @NotNull(message = "To date is required")
    private LocalDate to;
}
