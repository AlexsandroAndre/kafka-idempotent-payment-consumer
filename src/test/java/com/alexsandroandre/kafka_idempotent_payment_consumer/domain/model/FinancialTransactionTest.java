package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FinancialTransactionTest {

    @Test
    void shouldCreateCreditTransaction() {
        UUID accountId = UUID.randomUUID();

        FinancialTransaction transaction = FinancialTransaction.credit(
                "txn-001", accountId, BigDecimal.valueOf(100), "BRL");

        assertThat(transaction.getId()).isNotNull();
        assertThat(transaction.getTransactionId()).isEqualTo("txn-001");
        assertThat(transaction.getAccountId()).isEqualTo(accountId);
        assertThat(transaction.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(transaction.getCurrency()).isEqualTo("BRL");
        assertThat(transaction.getTransactionType()).isEqualTo(FinancialTransaction.TransactionType.CREDIT);
        assertThat(transaction.getCreatedAt()).isNull();
    }

    @Test
    void shouldThrowWhenAmountIsZeroOrNegative() {
        UUID accountId = UUID.randomUUID();

        assertThatThrownBy(() -> FinancialTransaction.credit("txn-001", accountId, BigDecimal.ZERO, "BRL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FinancialTransaction.TRANSACTION_AMOUNT_MUST_BE_POSITIVE);

        assertThatThrownBy(() -> FinancialTransaction.credit("txn-001", accountId, BigDecimal.valueOf(-1), "BRL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FinancialTransaction.TRANSACTION_AMOUNT_MUST_BE_POSITIVE);
    }

    @Test
    void shouldThrowWhenTransactionIdIsNull() {
        UUID accountId = UUID.randomUUID();

        assertThatThrownBy(() -> FinancialTransaction.credit(null, accountId, BigDecimal.TEN, "BRL"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenAccountIdIsNull() {
        assertThatThrownBy(() -> FinancialTransaction.credit("txn-001", null, BigDecimal.TEN, "BRL"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowWhenCurrencyIsNull() {
        UUID accountId = UUID.randomUUID();

        assertThatThrownBy(() -> FinancialTransaction.credit("txn-001", accountId, BigDecimal.TEN, null))
                .isInstanceOf(NullPointerException.class);
    }
}
