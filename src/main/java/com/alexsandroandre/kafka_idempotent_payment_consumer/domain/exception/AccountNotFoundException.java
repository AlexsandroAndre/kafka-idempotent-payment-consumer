package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.exception;

public final class AccountNotFoundException extends RuntimeException {

    public static final String ACCOUNT_NOT_FOUND = "Account not found: ";

    public AccountNotFoundException(String accountId) {
        super(ACCOUNT_NOT_FOUND + accountId);
    }
}