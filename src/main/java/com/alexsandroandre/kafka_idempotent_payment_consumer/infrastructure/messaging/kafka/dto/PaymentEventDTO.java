package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentEventDTO(
        String eventId,
        String idempotencyKey,
        UUID accountId,
        BigDecimal amount,
        String currency) {
}
