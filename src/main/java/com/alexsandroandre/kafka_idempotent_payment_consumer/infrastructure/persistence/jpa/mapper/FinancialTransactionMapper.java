package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.FinancialTransaction;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.FinancialTransactionEntity;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.time.OffsetDateTime;

@Component
public class FinancialTransactionMapper {

    public static final String FAILED_TO_MAP_FINANCIAL_TRANSACTION_ENTITY_TO_FINANCIAL_TRANSACTION = "Failed to map FinancialTransactionEntity to FinancialTransaction";

    public FinancialTransactionEntity toEntity(FinancialTransaction financialTransaction) {
        OffsetDateTime createdAt = financialTransaction.getCreatedAt() != null
                ? financialTransaction.getCreatedAt()
                : OffsetDateTime.now();

        return FinancialTransactionEntity.builder()
                .id(financialTransaction.getId())
                .transactionId(financialTransaction.getTransactionId())
                .accountId(financialTransaction.getAccountId())
                .amount(financialTransaction.getAmount())
                .currency(financialTransaction.getCurrency())
                .transactionType(financialTransaction.getTransactionType().name())
                .createdAt(createdAt)
                .build();
    }

    public FinancialTransaction toDomain(FinancialTransactionEntity financialTransactionEntity) {
        try {
            Constructor<FinancialTransaction> constructor = FinancialTransaction.class.getDeclaredConstructor(
                    java.util.UUID.class,
                    String.class,
                    java.util.UUID.class,
                    java.math.BigDecimal.class,
                    String.class,
                    FinancialTransaction.TransactionType.class,
                    OffsetDateTime.class);
            constructor.setAccessible(true);
            return constructor.newInstance(
                    financialTransactionEntity.getId(),
                    financialTransactionEntity.getTransactionId(),
                    financialTransactionEntity.getAccountId(),
                    financialTransactionEntity.getAmount(),
                    financialTransactionEntity.getCurrency(),
                    FinancialTransaction.TransactionType.valueOf(financialTransactionEntity.getTransactionType()),
                    financialTransactionEntity.getCreatedAt());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(FAILED_TO_MAP_FINANCIAL_TRANSACTION_ENTITY_TO_FINANCIAL_TRANSACTION, e);
        }
    }
}
