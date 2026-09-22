package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.consumer;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.PaymentResult;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentCommand;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentUseCase;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.exception.InvalidPaymentException;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.dto.PaymentEventDTO;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.mapper.PaymentEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);
    public static final String EVENTO_DE_PAGAMENTO_NAO_PODE_SER_NULO = "Evento de pagamento não pode ser nulo";
    public static final String EVENT_ID_E_OBRIGATORIO = "eventId é obrigatório";
    public static final String IDEMPOTENCY_KEY_E_OBRIGATORIO = "idempotencyKey é obrigatório";
    public static final String ACCOUNT_ID_E_OBRIGATORIO = "accountId é obrigatório";
    public static final String AMOUNT_DEVE_SER_POSITIVO = "amount deve ser positivo";
    public static final String CURRENCY_DEVE_CONTER_3_CARACTERES = "currency deve conter 3 caracteres";

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final PaymentEventMapper paymentEventMapper;

    public PaymentEventListener(ProcessPaymentUseCase processPaymentUseCase, PaymentEventMapper paymentEventMapper) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.paymentEventMapper = paymentEventMapper;
    }

    @KafkaListener(topics = "${payment.kafka.topic}", containerFactory = "paymentEventKafkaListenerContainerFactory")
    public void onMessage(PaymentEventDTO event,
                          Acknowledgment acknowledgment,
                          @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                          @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            validate(event);
            ProcessPaymentCommand command = paymentEventMapper.toCommand(event);
            PaymentResult result = processPaymentUseCase.process(command);
            log.info("Evento de pagamento processado. eventId={}, idempotencyKey={}, status={}, reference={}, partition={}, offset={}",
                    event.eventId(), event.idempotencyKey(), result.status(), result.reference(), partition, offset);
            acknowledgment.acknowledge();
        } catch (InvalidPaymentException ex) {
            log.warn("Evento de pagamento inválido. eventId={}, idempotencyKey={}, partition={}, offset={}, reason={}",
                    event != null ? event.eventId() : null,
                    event != null ? event.idempotencyKey() : null,
                    partition,
                    offset,
                    ex.getMessage());
            acknowledgment.acknowledge();
        }
    }

    private void validate(PaymentEventDTO event) {
        if (event == null) {
            throw new InvalidPaymentException(EVENTO_DE_PAGAMENTO_NAO_PODE_SER_NULO);
        }
        if (event.eventId() == null || event.eventId().isBlank()) {
            throw new InvalidPaymentException(EVENT_ID_E_OBRIGATORIO);
        }
        if (event.idempotencyKey() == null || event.idempotencyKey().isBlank()) {
            throw new InvalidPaymentException(IDEMPOTENCY_KEY_E_OBRIGATORIO);
        }
        if (event.accountId() == null) {
            throw new InvalidPaymentException(ACCOUNT_ID_E_OBRIGATORIO);
        }
        if (event.amount() == null || event.amount().signum() <= 0) {
            throw new InvalidPaymentException(AMOUNT_DEVE_SER_POSITIVO);
        }
        if (event.currency() == null || event.currency().isBlank() || event.currency().length() != 3) {
            throw new InvalidPaymentException(CURRENCY_DEVE_CONTER_3_CARACTERES);
        }
    }
}
