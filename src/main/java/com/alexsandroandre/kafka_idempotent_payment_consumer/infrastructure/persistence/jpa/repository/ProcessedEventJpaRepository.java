package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository;

import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface ProcessedEventJpaRepository extends JpaRepository<ProcessedEventEntity, UUID> {

    @Modifying
    @Query(value = """
        INSERT INTO processed_events (id, idempotency_key, event_id, transaction_id, payload_hash, status, processed_at)
        VALUES (:id, :idempotencyKey, :eventId, :transactionId, :payloadHash, :status, :processedAt)
        ON CONFLICT (idempotency_key) DO NOTHING
        """, nativeQuery = true)
    int claim(@Param("id") UUID id,
              @Param("idempotencyKey") String idempotencyKey,
              @Param("eventId") String eventId,
              @Param("transactionId") String transactionId,
              @Param("payloadHash") String payloadHash,
              @Param("status") String status,
              @Param("processedAt") OffsetDateTime processedAt);
}
