package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.FinancialTransaction;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.FinancialTransactionEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FinancialTransactionMapperTest {

    public static final String TRANSACTION_ID = "txn-200";
    public static final String CURRENCY = "BRL";
    public static final String EXPECTED = "txn-200";
    private final FinancialTransactionMapper financialTransactionMapper = new FinancialTransactionMapper();

    @Test
    void shouldMapDomainToEntityAndBack() {
        FinancialTransaction financialTransaction = FinancialTransaction.credit(
                TRANSACTION_ID,
                UUID.randomUUID(),
                BigDecimal.valueOf(99.50),
                CURRENCY);

        OffsetDateTime createdAt = OffsetDateTime.now();
        FinancialTransactionEntity entity = FinancialTransactionEntity.builder()
                .id(financialTransaction.getId())
                .transactionId(financialTransaction.getTransactionId())
                .accountId(financialTransaction.getAccountId())
                .amount(financialTransaction.getAmount())
                .currency(financialTransaction.getCurrency())
                .transactionType(financialTransaction.getTransactionType().name())
                .createdAt(createdAt)
                .build();

        FinancialTransaction mappedDomain = financialTransactionMapper.toDomain(entity);

        assertThat(mappedDomain.getId()).isEqualTo(financialTransaction.getId());
        assertThat(mappedDomain.getTransactionId()).isEqualTo(EXPECTED);
        assertThat(mappedDomain.getTransactionType()).isEqualTo(FinancialTransaction.TransactionType.CREDIT);
        assertThat(mappedDomain.getCreatedAt()).isEqualTo(createdAt);
    }
}
