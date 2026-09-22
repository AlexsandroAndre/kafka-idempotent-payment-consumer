package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentCommand;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.dto.PaymentEventDTO;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

@Component
public class PaymentEventMapper {

    public static final String SHA_256 = "SHA-256";
    public static final String UNABLE_TO_CALCULATE_PAYLOAD_HASH = "Unable to calculate payload hash";

    public ProcessPaymentCommand toCommand(PaymentEventDTO event) {
        return new ProcessPaymentCommand(
                event.idempotencyKey(),
                event.eventId(),
                UUID.randomUUID().toString(),
                event.accountId(),
                event.amount(),
                event.currency(),
                payloadHash(event));
    }

    private String payloadHash(PaymentEventDTO event) {
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA_256);
            String payload = event.eventId() + "|" + event.idempotencyKey() + "|" + event.accountId() + "|" + event.amount() + "|" + event.currency();
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(UNABLE_TO_CALCULATE_PAYLOAD_HASH, e);
        }
    }
}
