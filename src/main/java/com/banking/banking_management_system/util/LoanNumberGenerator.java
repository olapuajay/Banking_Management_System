package com.banking.banking_management_system.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class LoanNumberGenerator {
    private final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder number = new StringBuilder("LN");

        for(int i = 0; i < 12; i++) {
            number.append(random.nextInt(10));
        }

        return number.toString();
    }
}
