package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository;

import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.FinancialTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialTransactionJpaRepository extends JpaRepository<FinancialTransactionEntity, UUID> {
}
