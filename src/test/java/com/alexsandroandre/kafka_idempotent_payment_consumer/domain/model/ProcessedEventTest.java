package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProcessedEventTest {

    @Test
    void shouldClaimProcessedEvent() {
        ProcessedEvent event = ProcessedEvent.claim("idem-key-1", "event-1", "txn-1", "hash-1");

        assertThat(event.getId()).isNotNull();
        assertThat(event.getIdempotencyKey()).isEqualTo("idem-key-1");
        assertThat(event.getEventId()).isEqualTo("event-1");
        assertThat(event.getTransactionId()).isEqualTo("txn-1");
        assertThat(event.getPayloadHash()).isEqualTo("hash-1");
        assertThat(event.getStatus()).isEqualTo(ProcessedEvent.ProcessedEventStatus.PROCESSED);
        assertThat(event.getProcessedAt()).isNull();
    }

    @Test
    void shouldThrowWhenIdempotencyKeyIsNull() {
        assertThatThrownBy(() -> ProcessedEvent.claim(null, "event-1", "txn-1", "hash-1"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenEventIdIsNull() {
        assertThatThrownBy(() -> ProcessedEvent.claim("idem-key-1", null, "txn-1", "hash-1"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenTransactionIdIsNull() {
        assertThatThrownBy(() -> ProcessedEvent.claim("idem-key-1", "event-1", null, "hash-1"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenPayloadHashIsNull() {
        assertThatThrownBy(() -> ProcessedEvent.claim("idem-key-1", "event-1", "txn-1", null))
                .isInstanceOf(NullPointerException.class);
    }
}
