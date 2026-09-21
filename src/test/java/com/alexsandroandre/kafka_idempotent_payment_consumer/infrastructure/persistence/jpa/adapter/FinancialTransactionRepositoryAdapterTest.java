package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.adapter;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.FinancialTransaction;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.FinancialTransactionEntity;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper.FinancialTransactionMapper;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository.FinancialTransactionJpaRepository;
import com.alexsandroandre.kafka_idempotent_payment_consumer.support.PostgresIntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FinancialTransactionRepositoryAdapterTest extends PostgresIntegrationTestBase {

    private static final UUID SEEDED_ACCOUNT_ID = UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d");
    public static final String TRANSACTION_ID = "txn-100";
    public static final String CURRENCY = "BRL";
    public static final String EXPECTED = "txn-100";

    @Autowired
    private FinancialTransactionRepositoryAdapter financialTransactionRepositoryAdapter;

    @Autowired
    private FinancialTransactionJpaRepository financialTransactionJpaRepository;

    @Autowired
    private FinancialTransactionMapper financialTransactionMapper;

    @Test
    void shouldSaveAndReturnPersistedDomain() {
        FinancialTransaction financialTransaction = FinancialTransaction.credit(
                TRANSACTION_ID, SEEDED_ACCOUNT_ID, BigDecimal.valueOf(125), CURRENCY);

        FinancialTransaction saved = financialTransactionRepositoryAdapter.save(financialTransaction);

        assertThat(saved.getId()).isEqualTo(financialTransaction.getId());
        assertThat(saved.getTransactionId()).isEqualTo(EXPECTED);
        assertThat(saved.getAccountId()).isEqualTo(SEEDED_ACCOUNT_ID);
        assertThat(saved.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(125));

        Optional<FinancialTransactionEntity> persisted = financialTransactionJpaRepository.findById(saved.getId());
        assertThat(persisted).isPresent();
        assertThat(financialTransactionMapper.toDomain(persisted.get()).getTransactionId()).isEqualTo(EXPECTED);
    }
}
