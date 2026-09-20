package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.exception;

public final class InvalidPaymentException extends RuntimeException {
    public InvalidPaymentException(String message) {
        super(message);
    }
}