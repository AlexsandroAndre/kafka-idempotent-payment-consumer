package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidPaymentExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        InvalidPaymentException exception = new InvalidPaymentException("invalid amount");

        assertThat(exception.getMessage()).isEqualTo("invalid amount");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
