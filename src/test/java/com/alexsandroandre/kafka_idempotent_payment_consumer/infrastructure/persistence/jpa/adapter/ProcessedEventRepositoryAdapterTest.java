package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.adapter;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.ProcessedEvent;
import com.alexsandroandre.kafka_idempotent_payment_consumer.support.PostgresIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProcessedEventRepositoryAdapterTest extends PostgresIntegrationTestBase {

    public static final String IDEMPOTENCY_KEY = "idem-key-adapter-1";
    public static final String EVENT_ID = "event-adapter-1";
    public static final String TRANSACTION_ID = "txn-adapter-1";
    public static final String PAYLOAD_HASH = "hash-adapter-1";
    
    @Autowired
    private ProcessedEventRepositoryAdapter processedEventRepositoryAdapter;

    @Test
    void shouldClaimEventThroughAdapter() {
        ProcessedEvent processedEvent = ProcessedEvent.claim(
                IDEMPOTENCY_KEY,
                EVENT_ID,
                TRANSACTION_ID,
                PAYLOAD_HASH);

        int affected = processedEventRepositoryAdapter.claim(processedEvent);

        assertThat(affected).isEqualTo(1);
    }
}
