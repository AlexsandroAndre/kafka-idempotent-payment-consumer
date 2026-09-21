package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class ProcessedEvent {

    private final UUID id;
    private final String idempotencyKey;
    private final String eventId;
    private final String transactionId;
    private final String payloadHash;
    private final ProcessedEventStatus status;
    private final OffsetDateTime processedAt;

    private ProcessedEvent(UUID id,
                           String idempotencyKey,
                           String eventId,
                           String transactionId,
                           String payloadHash,
                           ProcessedEventStatus status,
                           OffsetDateTime processedAt) {
        this.id = id;
        this.idempotencyKey = Objects.requireNonNull(idempotencyKey);
        this.eventId = Objects.requireNonNull(eventId);
        this.transactionId = Objects.requireNonNull(transactionId);
        this.payloadHash = Objects.requireNonNull(payloadHash);
        this.status = Objects.requireNonNull(status);
        this.processedAt = processedAt;
    }

    public static ProcessedEvent claim(String idempotencyKey,
                                       String eventId,
                                       String transactionId,
                                       String payloadHash) {
        return new ProcessedEvent(
                UUID.randomUUID(),
                idempotencyKey,
                eventId, transactionId,
                payloadHash,
                ProcessedEventStatus.PROCESSED,
                null);
    }

    public UUID getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getEventId() { return eventId; }
    public String getTransactionId() { return transactionId; }
    public String getPayloadHash() { return payloadHash; }
    public ProcessedEventStatus getStatus() { return status; }
    public OffsetDateTime getProcessedAt() { return processedAt; }

    public enum ProcessedEventStatus { PROCESSED }
}
