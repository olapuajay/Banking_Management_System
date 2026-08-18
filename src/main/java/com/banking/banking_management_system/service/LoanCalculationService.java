package com.banking.banking_management_system.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class LoanCalculationService {
    public BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualInterestRate, int tenureMonths) {
        BigDecimal monthlyRate = annualInterestRate
                .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

        double p = principal.doubleValue();
        double r = monthlyRate.doubleValue();
        int n = tenureMonths;

        double emi = p * r * Math.pow(1 + r, n) / (Math.pow(1 + r, n) - 1);

        return BigDecimal.valueOf(emi)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
