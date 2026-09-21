package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository;

import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.FinancialTransactionEntity;
import com.alexsandroandre.kafka_idempotent_payment_consumer.support.PostgresIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FinancialTransactionJpaRepositoryTest extends PostgresIntegrationTestBase {

    private static final UUID SEEDED_ACCOUNT_ID = UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d");

    @Autowired
    private FinancialTransactionJpaRepository financialTransactionJpaRepository;

    @Test
    void shouldSaveAndFindFinancialTransaction() {
        FinancialTransactionEntity entity = FinancialTransactionEntity.builder()
                .id(UUID.randomUUID())
                .transactionId("txn-001")
                .accountId(SEEDED_ACCOUNT_ID)
                .amount(BigDecimal.valueOf(150))
                .currency("BRL")
                .transactionType("CREDIT")
                .createdAt(OffsetDateTime.now())
                .build();

        FinancialTransactionEntity saved = financialTransactionJpaRepository.saveAndFlush(entity);

        Optional<FinancialTransactionEntity> found = financialTransactionJpaRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTransactionId()).isEqualTo("txn-001");
        assertThat(found.get().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(150));
        assertThat(found.get().getAccountId()).isEqualTo(SEEDED_ACCOUNT_ID);
    }

    @Test
    void shouldRejectTransactionWithNonPositiveAmount() {
        FinancialTransactionEntity entity = FinancialTransactionEntity.builder()
                .id(UUID.randomUUID())
                .transactionId("txn-002")
                .accountId(SEEDED_ACCOUNT_ID)
                .amount(BigDecimal.ZERO)
                .currency("BRL")
                .transactionType("CREDIT")
                .createdAt(OffsetDateTime.now())
                .build();

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> financialTransactionJpaRepository.saveAndFlush(entity));
    }
}
