package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.adapter;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.Account;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.AccountEntity;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository.AccountJpaRepository;
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
class AccountRepositoryAdapterTest extends PostgresIntegrationTestBase {

    private static final UUID SEEDED_ACCOUNT_ID = UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d");
    public static final String ACC_100 = "ACC-100";
    public static final String ALICE = "Alice";
    public static final String CURRENCY = "BRL";

    @Autowired
    private AccountRepositoryAdapter accountRepositoryAdapter;

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Test
    void shouldSaveAndReturnPersistedAccount() {
        Account account = new Account(
                UUID.randomUUID(),
                ACC_100,
                ALICE,
                CURRENCY,
                BigDecimal.valueOf(1000),
                null,
                null);

        Account saved = accountRepositoryAdapter.save(account);

        assertThat(saved.getId()).isEqualTo(account.getId());
        assertThat(saved.getAccountNumber()).isEqualTo(ACC_100);
        assertThat(saved.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1000));

        Optional<AccountEntity> persisted = accountJpaRepository.findById(saved.getId());
        assertThat(persisted).isPresent();
        assertThat(persisted.get().getAccountNumber()).isEqualTo(ACC_100);
    }
}
