package com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in;

public record PaymentResult(PaymentStatus status, String reference) {

    public static PaymentResult processed(String transactionId) {
        return new PaymentResult(PaymentStatus.PROCESSED, transactionId);
    }

    public static PaymentResult duplicate(String idempotencyKey) {
        return new PaymentResult(PaymentStatus.DUPLICATE, idempotencyKey);
    }

    public enum PaymentStatus {
        PROCESSED,
        DUPLICATE
    }
}
