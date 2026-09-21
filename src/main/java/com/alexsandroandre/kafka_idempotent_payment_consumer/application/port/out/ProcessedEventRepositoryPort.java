package com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.ProcessedEvent;

public interface ProcessedEventRepositoryPort {

    /**
     * Tenta reivindicar a chave de idempotência via {@code INSERT ... ON CONFLICT DO NOTHING}.
     *
     * @return {@code 1} se o evento foi reivindicado agora (primeira vez),
     *         {@code 0} se a chave já existia (duplicata).
     */
    int claim(ProcessedEvent processedEvent);
}
