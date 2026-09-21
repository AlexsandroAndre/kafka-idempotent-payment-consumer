package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessedEventEntityTest {

    @Test
    void shouldBuildEntityWithBuilder() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        ProcessedEventEntity entity = ProcessedEventEntity.builder()
                .id(id)
                .idempotencyKey("idem-key-1")
                .eventId("event-1")
                .transactionId("txn-1")
                .payloadHash("hash-1")
                .status("PROCESSED")
                .processedAt(now)
                .build();

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getIdempotencyKey()).isEqualTo("idem-key-1");
        assertThat(entity.getEventId()).isEqualTo("event-1");
        assertThat(entity.getTransactionId()).isEqualTo("txn-1");
        assertThat(entity.getPayloadHash()).isEqualTo("hash-1");
        assertThat(entity.getStatus()).isEqualTo("PROCESSED");
        assertThat(entity.getProcessedAt()).isEqualTo(now);
    }

    @Test
    void shouldAllowFieldMutationViaSetters() {
        ProcessedEventEntity entity = new ProcessedEventEntity();

        entity.setIdempotencyKey("idem-key-2");
        entity.setEventId("event-2");
        entity.setStatus("PROCESSED");

        assertThat(entity.getIdempotencyKey()).isEqualTo("idem-key-2");
        assertThat(entity.getEventId()).isEqualTo("event-2");
        assertThat(entity.getStatus()).isEqualTo("PROCESSED");
    }

    @Test
    void shouldCreateEntityWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        ProcessedEventEntity entity = new ProcessedEventEntity(
                id, "idem-key-3", "event-3", "txn-3", "hash-3", "PROCESSED", now);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getIdempotencyKey()).isEqualTo("idem-key-3");
    }
}
