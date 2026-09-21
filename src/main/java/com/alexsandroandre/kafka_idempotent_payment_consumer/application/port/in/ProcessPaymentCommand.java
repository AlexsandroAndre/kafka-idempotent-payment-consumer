package com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public record ProcessPaymentCommand(
        String idempotencyKey,
        String eventId,
        String transactionId,
        UUID accountId,
        BigDecimal amount,
        String currency,
        String payloadHash) {
}
