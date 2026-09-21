package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository;

import com.alexsandroandre.kafka_idempotent_payment_consumer.support.PostgresIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProcessedEventJpaRepositoryTest extends PostgresIntegrationTestBase {

    @Autowired
    private ProcessedEventJpaRepository processedEventJpaRepository;

    @Test
    void shouldClaimEventOnFirstAttempt() {
        int affected = processedEventJpaRepository.claim(
                UUID.randomUUID(),
                "idem-key-1",
                "event-1",
                "txn-1",
                "hash-1",
                "PROCESSED",
                OffsetDateTime.now());

        assertThat(affected).isEqualTo(1);
    }

    @Test
    void shouldNotClaimSameIdempotencyKeyTwice() {
        String idempotencyKey = "idem-key-duplicate";

        int firstAttempt = processedEventJpaRepository.claim(
                UUID.randomUUID(), idempotencyKey, "event-1", "txn-1", "hash-1",
                "PROCESSED", OffsetDateTime.now());

        int secondAttempt = processedEventJpaRepository.claim(
                UUID.randomUUID(), idempotencyKey, "event-2", "txn-2", "hash-2",
                "PROCESSED", OffsetDateTime.now());

        assertThat(firstAttempt).isEqualTo(1);
        assertThat(secondAttempt).isEqualTo(0);
        assertThat(processedEventJpaRepository.findAll())
                .filteredOn(e -> e.getIdempotencyKey().equals(idempotencyKey))
                .hasSize(1);
    }
}
