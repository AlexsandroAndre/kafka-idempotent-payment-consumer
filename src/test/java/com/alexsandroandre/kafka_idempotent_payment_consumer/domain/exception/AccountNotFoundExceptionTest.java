package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AccountNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithAccountId() {
        AccountNotFoundException exception = new AccountNotFoundException("acc-123");

        assertThat(exception.getMessage())
                .isEqualTo(AccountNotFoundException.ACCOUNT_NOT_FOUND + "acc-123");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
