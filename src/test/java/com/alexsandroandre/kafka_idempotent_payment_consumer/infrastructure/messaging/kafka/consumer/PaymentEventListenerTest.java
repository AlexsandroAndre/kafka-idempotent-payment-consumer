package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.consumer;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.PaymentResult;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentCommand;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentUseCase;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.dto.PaymentEventDTO;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.mapper.PaymentEventMapper;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

class PaymentEventListenerTest {

    public static final String EVENT_ID = "event-1";
    public static final String IDEMPOTENCY_KEY = "idem-1";
    public static final String UUID = "11111111-1111-1111-1111-111111111111";
    public static final String VAL = "10.50";
    public static final String CURRENCY = "BRL";
    public static final String TRANSACTION_ID = "tx-1";

    private final ProcessPaymentUseCase processPaymentUseCase = mock(ProcessPaymentUseCase.class);
    private final PaymentEventMapper paymentEventMapper = new PaymentEventMapper();
    private final PaymentEventListener listener = new PaymentEventListener(processPaymentUseCase, paymentEventMapper);
    private final Acknowledgment acknowledgment = mock(Acknowledgment.class);

    @Test
    void shouldAcknowledgeProcessedEvent() {
        PaymentEventDTO event = new PaymentEventDTO(
                EVENT_ID,
                IDEMPOTENCY_KEY,
                java.util.UUID.fromString(UUID),
                new BigDecimal(VAL),
                CURRENCY);
        when(processPaymentUseCase.process(org.mockito.ArgumentMatchers.any(ProcessPaymentCommand.class)))
                .thenReturn(PaymentResult.processed(TRANSACTION_ID));

        listener.onMessage(event, acknowledgment, 0, 1L);

        verify(acknowledgment).acknowledge();
    }

    @Test
    void shouldAcknowledgeInvalidEventWithoutProcessing() {
        PaymentEventDTO event = new PaymentEventDTO(
                "",
                IDEMPOTENCY_KEY,
                java.util.UUID.fromString(UUID),
                new BigDecimal(VAL),
                CURRENCY);

        listener.onMessage(event, acknowledgment, 0, 1L);

        verify(acknowledgment).acknowledge();
        verify(processPaymentUseCase, never()).process(org.mockito.ArgumentMatchers.any());
    }
}
