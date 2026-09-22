package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentCommand;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.dto.PaymentEventDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentEventMapperTest {

    public static final String EVENT_ID = "event-1";
    public static final String IDEMPOTENCY_KEY = "idem-1";
    public static final String UUID = "11111111-1111-1111-1111-111111111111";
    public static final String VAL = "10.50";
    public static final String CURRENCY = "BRL";
    
    private final PaymentEventMapper mapper = new PaymentEventMapper();

    @Test
    void shouldMapPaymentEventToCommand() {
        PaymentEventDTO event = new PaymentEventDTO(
                EVENT_ID,
                IDEMPOTENCY_KEY,
                java.util.UUID.fromString(UUID),
                new BigDecimal(VAL),
                CURRENCY);

        ProcessPaymentCommand command = mapper.toCommand(event);

        assertThat(command.eventId()).isEqualTo(EVENT_ID);
        assertThat(command.idempotencyKey()).isEqualTo(IDEMPOTENCY_KEY);
        assertThat(command.accountId()).isEqualTo(event.accountId());
        assertThat(command.amount()).isEqualByComparingTo(VAL);
        assertThat(command.currency()).isEqualTo(CURRENCY);
        assertThat(command.transactionId()).isNotBlank();
        Assertions.assertThat(java.util.UUID.fromString(command.transactionId())).isNotNull();
        assertThat(command.payloadHash()).isNotBlank();
    }
}
