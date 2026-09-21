package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.ProcessedEvent;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.ProcessedEventEntity;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessedEventMapperTest {

    public static final String IDEMPOTENCY_KEY = "idem-key-100";
    public static final String EVENT_ID = "event-100";
    public static final String TRANSACTION_ID = "txn-100";
    public static final String PAYLOAD_HASH = "hash-100";
    public static final String EXPECTED = "idem-key-100";
    private final ProcessedEventMapper processedEventMapper = new ProcessedEventMapper();

    @Test
    void shouldMapDomainToEntityAndBack() {
        ProcessedEvent processedEvent = ProcessedEvent.claim(IDEMPOTENCY_KEY, EVENT_ID, TRANSACTION_ID, PAYLOAD_HASH);
        OffsetDateTime processedAt = OffsetDateTime.now();

        ProcessedEventEntity entity = ProcessedEventEntity.builder()
                .id(processedEvent.getId())
                .idempotencyKey(processedEvent.getIdempotencyKey())
                .eventId(processedEvent.getEventId())
                .transactionId(processedEvent.getTransactionId())
                .payloadHash(processedEvent.getPayloadHash())
                .status(processedEvent.getStatus().name())
                .processedAt(processedAt)
                .build();

        ProcessedEvent mappedDomain = processedEventMapper.toDomain(entity);

        assertThat(mappedDomain.getId()).isEqualTo(processedEvent.getId());
        assertThat(mappedDomain.getIdempotencyKey()).isEqualTo(EXPECTED);
        assertThat(mappedDomain.getStatus()).isEqualTo(ProcessedEvent.ProcessedEventStatus.PROCESSED);
        assertThat(mappedDomain.getProcessedAt()).isEqualTo(processedAt);
    }
}
