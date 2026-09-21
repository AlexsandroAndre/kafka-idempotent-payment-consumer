package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.ProcessedEvent;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.ProcessedEventEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.time.OffsetDateTime;

@Component
public class ProcessedEventMapper {

    public static final String FAILED_TO_MAP_PROCESSED_EVENT_ENTITY_TO_PROCESSED_EVENT = "Failed to map ProcessedEventEntity to ProcessedEvent";

    public ProcessedEventEntity toEntity(ProcessedEvent processedEvent) {
        OffsetDateTime processedAt = processedEvent.getProcessedAt() != null
                ? processedEvent.getProcessedAt()
                : OffsetDateTime.now();

        return ProcessedEventEntity.builder()
                .id(processedEvent.getId())
                .idempotencyKey(processedEvent.getIdempotencyKey())
                .eventId(processedEvent.getEventId())
                .transactionId(processedEvent.getTransactionId())
                .payloadHash(processedEvent.getPayloadHash())
                .status(processedEvent.getStatus().name())
                .processedAt(processedAt)
                .build();
    }

    public ProcessedEvent toDomain(ProcessedEventEntity processedEventEntity) {
        try {
            Constructor<ProcessedEvent> constructor = ProcessedEvent.class.getDeclaredConstructor(
                    java.util.UUID.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    ProcessedEvent.ProcessedEventStatus.class,
                    OffsetDateTime.class);
            constructor.setAccessible(true);
            return constructor.newInstance(
                    processedEventEntity.getId(),
                    processedEventEntity.getIdempotencyKey(),
                    processedEventEntity.getEventId(),
                    processedEventEntity.getTransactionId(),
                    processedEventEntity.getPayloadHash(),
                    ProcessedEvent.ProcessedEventStatus.valueOf(processedEventEntity.getStatus()),
                    processedEventEntity.getProcessedAt());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(FAILED_TO_MAP_PROCESSED_EVENT_ENTITY_TO_PROCESSED_EVENT, e);
        }
    }
}
