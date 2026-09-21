package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FinancialTransactionEntityTest {

    @Test
    void shouldBuildEntityWithBuilder() {
        UUID id = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        FinancialTransactionEntity entity = FinancialTransactionEntity.builder()
                .id(id)
                .transactionId("txn-001")
                .accountId(accountId)
                .amount(BigDecimal.valueOf(150))
                .currency("BRL")
                .transactionType("CREDIT")
                .createdAt(now)
                .build();

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getTransactionId()).isEqualTo("txn-001");
        assertThat(entity.getAccountId()).isEqualTo(accountId);
        assertThat(entity.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(entity.getCurrency()).isEqualTo("BRL");
        assertThat(entity.getTransactionType()).isEqualTo("CREDIT");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
    }

    @Test
    void shouldAllowFieldMutationViaSetters() {
        FinancialTransactionEntity entity = new FinancialTransactionEntity();

        entity.setTransactionId("txn-002");
        entity.setAmount(BigDecimal.ONE);
        entity.setCurrency("USD");
        entity.setTransactionType("CREDIT");

        assertThat(entity.getTransactionId()).isEqualTo("txn-002");
        assertThat(entity.getAmount()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(entity.getCurrency()).isEqualTo("USD");
        assertThat(entity.getTransactionType()).isEqualTo("CREDIT");
    }

    @Test
    void shouldCreateEntityWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        FinancialTransactionEntity entity = new FinancialTransactionEntity(
                id, "txn-003", accountId, BigDecimal.TEN, "BRL", "CREDIT", now);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getTransactionId()).isEqualTo("txn-003");
    }
}
