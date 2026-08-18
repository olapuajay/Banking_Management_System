package com.banking.banking_management_system.config;

import com.banking.banking_management_system.enums.LoanType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class LoanInterestRateConfig {
    private final Map<LoanType, BigDecimal> rates = Map.of(
            LoanType.PERSONAL, new BigDecimal("12.00"),
            LoanType.HOME, new BigDecimal("8.50"),
            LoanType.EDUCATION, new BigDecimal("7.50"),
            LoanType.VEHICLE, new BigDecimal("9.00")
    );

    public BigDecimal getRate(LoanType loanType) {
        return rates.get(loanType);
    }
}
