package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    private Account newAccount(BigDecimal balance) {
        return new Account(
                UUID.randomUUID(),
                "ACC-0001",
                "Alice Nakamoto Corp",
                "BRL",
                balance,
                OffsetDateTime.now(),
                OffsetDateTime.now());
    }

    @Test
    void shouldCreateAccountWithAllFields() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        Account account = new Account(id, "ACC-0001", "Alice Nakamoto Corp", "BRL",
                BigDecimal.TEN, createdAt, updatedAt);

        assertThat(account.getId()).isEqualTo(id);
        assertThat(account.getAccountNumber()).isEqualTo("ACC-0001");
        assertThat(account.getHolderName()).isEqualTo("Alice Nakamoto Corp");
        assertThat(account.getCurrency()).isEqualTo("BRL");
        assertThat(account.getBalance()).isEqualTo(BigDecimal.TEN);
        assertThat(account.getCreatedAt()).isEqualTo(createdAt);
        assertThat(account.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void shouldThrowWhenRequiredFieldIsNull() {
        assertThatThrownBy(() -> new Account(null, "ACC-0001", "Alice", "BRL",
                BigDecimal.TEN, OffsetDateTime.now(), OffsetDateTime.now()))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Account(UUID.randomUUID(), null, "Alice", "BRL",
                BigDecimal.TEN, OffsetDateTime.now(), OffsetDateTime.now()))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Account(UUID.randomUUID(), "ACC-0001", null, "BRL",
                BigDecimal.TEN, OffsetDateTime.now(), OffsetDateTime.now()))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Account(UUID.randomUUID(), "ACC-0001", "Alice", null,
                BigDecimal.TEN, OffsetDateTime.now(), OffsetDateTime.now()))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> new Account(UUID.randomUUID(), "ACC-0001", "Alice", "BRL",
                null, OffsetDateTime.now(), OffsetDateTime.now()))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldCreditAccountAndUpdateBalance() {
        Account account = newAccount(BigDecimal.valueOf(1000));

        account.credit(BigDecimal.valueOf(500), "BRL");

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1500));
        assertThat(account.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldThrowWhenCreditAmountIsNull() {
        Account account = newAccount(BigDecimal.valueOf(1000));

        assertThatThrownBy(() -> account.credit(null, "BRL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Account.CREDIT_AMOUNT_MUST_BE_POSITIVE);
    }

    @Test
    void shouldThrowWhenCreditAmountIsZeroOrNegative() {
        Account account = newAccount(BigDecimal.valueOf(1000));

        assertThatThrownBy(() -> account.credit(BigDecimal.ZERO, "BRL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Account.CREDIT_AMOUNT_MUST_BE_POSITIVE);

        assertThatThrownBy(() -> account.credit(BigDecimal.valueOf(-10), "BRL"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Account.CREDIT_AMOUNT_MUST_BE_POSITIVE);
    }

    @Test
    void shouldThrowWhenCurrencyMismatches() {
        Account account = newAccount(BigDecimal.valueOf(1000));

        assertThatThrownBy(() -> account.credit(BigDecimal.valueOf(100), "USD"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Account.CURRENCY_MISMATCH_ACCOUNT + "BRL" + Account.PAYMENT + "USD");
    }

    @Test
    void shouldAcceptCurrencyMismatchCaseInsensitive() {
        Account account = newAccount(BigDecimal.valueOf(1000));

        account.credit(BigDecimal.valueOf(100), "brl");

        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1100));
    }
}
