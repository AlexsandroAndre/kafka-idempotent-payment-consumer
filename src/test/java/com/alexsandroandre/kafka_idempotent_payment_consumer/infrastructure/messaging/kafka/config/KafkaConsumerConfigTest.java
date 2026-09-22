package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.config;

import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.messaging.kafka.dto.PaymentEventDTO;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import static org.assertj.core.api.Assertions.assertThat;

class KafkaConsumerConfigTest {

    private final KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig();

    @Test
    void shouldConfigureManualImmediateAckMode() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentEventDTO> factory = kafkaConsumerConfig.paymentEventKafkaListenerContainerFactory(
                kafkaConsumerConfig.paymentEventConsumerFactory());

        assertThat(factory.getContainerProperties().getAckMode())
                .isEqualTo(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
    }
}
