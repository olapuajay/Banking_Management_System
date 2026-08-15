package com.banking.banking_management_system.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class TransactionReferenceGenerator {
    private final SecureRandom random = new SecureRandom();

    public String generate() {
        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE);

        StringBuilder randomPart = new StringBuilder();

        for(int i = 0; i < 6; i++) {
            randomPart.append((char) ('A' + random.nextInt(26)));
        }

        return "TXN-" + date + "-" + randomPart;
    }
}
