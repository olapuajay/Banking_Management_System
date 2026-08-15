package com.banking.banking_management_system.exception;

public class AccountFrozenException extends RuntimeException {
    public AccountFrozenException(String message) {
        super(message);
    }
}
