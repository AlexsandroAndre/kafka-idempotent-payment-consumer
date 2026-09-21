package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.FinancialTransaction;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.FinancialTransactionEntity;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class FinancialTransactionMapper {

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
}
