package com.banking.banking_management_system.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class AccountNumberGenerator {
    private final SecureRandom random = new SecureRandom();

    public String generate() {
        StringBuilder accountNumber = new StringBuilder();

        for(int i = 0; i < 16; i++) {
            accountNumber.append(random.nextInt(10));
        }

        return accountNumber.toString();
    }
}
