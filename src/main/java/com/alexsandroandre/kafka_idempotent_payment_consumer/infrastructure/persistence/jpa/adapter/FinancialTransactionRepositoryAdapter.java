package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.adapter;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.FinancialTransactionRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.FinancialTransaction;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper.FinancialTransactionMapper;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository.FinancialTransactionJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class FinancialTransactionRepositoryAdapter implements FinancialTransactionRepositoryPort {

    private final FinancialTransactionJpaRepository financialTransactionJpaRepository;
    private final FinancialTransactionMapper financialTransactionMapper;

    public FinancialTransactionRepositoryAdapter(FinancialTransactionJpaRepository financialTransactionJpaRepository,
                                                  FinancialTransactionMapper financialTransactionMapper) {
        this.financialTransactionJpaRepository = financialTransactionJpaRepository;
        this.financialTransactionMapper = financialTransactionMapper;
    }

    @Override
    public FinancialTransaction save(FinancialTransaction financialTransaction) {
        financialTransactionJpaRepository.save(financialTransactionMapper.toEntity(financialTransaction));
        return financialTransaction;
    }
}
