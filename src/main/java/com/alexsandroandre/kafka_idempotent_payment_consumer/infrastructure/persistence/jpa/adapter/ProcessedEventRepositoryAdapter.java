package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.adapter;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.ProcessedEventRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.ProcessedEvent;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository.ProcessedEventJpaRepository;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class ProcessedEventRepositoryAdapter implements ProcessedEventRepositoryPort {

    private final ProcessedEventJpaRepository processedEventJpaRepository;

    public ProcessedEventRepositoryAdapter(ProcessedEventJpaRepository processedEventJpaRepository) {
        this.processedEventJpaRepository = processedEventJpaRepository;
    }

    @Override
    public int claim(ProcessedEvent processedEvent) {
        OffsetDateTime processedAt = processedEvent.getProcessedAt() != null
                ? processedEvent.getProcessedAt()
                : OffsetDateTime.now();

        return processedEventJpaRepository.claim(
                processedEvent.getId(),
                processedEvent.getIdempotencyKey(),
                processedEvent.getEventId(),
                processedEvent.getTransactionId(),
                processedEvent.getPayloadHash(),
                processedEvent.getStatus().name(),
                processedAt);
    }
}
