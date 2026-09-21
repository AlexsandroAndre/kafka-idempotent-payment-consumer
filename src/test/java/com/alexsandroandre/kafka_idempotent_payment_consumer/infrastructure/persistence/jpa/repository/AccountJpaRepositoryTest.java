package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository;

import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.AccountEntity;
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
class AccountJpaRepositoryTest extends PostgresIntegrationTestBase {

    @Autowired
    private AccountJpaRepository accountJpaRepository;

    @Test
    void shouldFindPreSeededAccount() {
        Optional<AccountEntity> account = accountJpaRepository
                .findById(UUID.fromString("a1b2c3d4-e5f6-7a8b-9c0d-1e2f3a4b5c6d"));

        assertThat(account).isPresent();
        assertThat(account.get().getAccountNumber()).isEqualTo("ACC-9988-1");
    }

    @Test
    void shouldSaveAndFindAccount() {
        AccountEntity entity = AccountEntity.builder()
                .id(UUID.randomUUID())
                .accountNumber("ACC-0001")
                .holderName("Alice")
                .currency("BRL")
                .balance(BigDecimal.valueOf(100))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        AccountEntity saved = accountJpaRepository.saveAndFlush(entity);

        Optional<AccountEntity> found = accountJpaRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getAccountNumber()).isEqualTo("ACC-0001");
        assertThat(found.get().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldReturnEmptyWhenAccountDoesNotExist() {
        Optional<AccountEntity> found = accountJpaRepository.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindAccountForUpdate() {
        AccountEntity entity = AccountEntity.builder()
                .id(UUID.randomUUID())
                .accountNumber("ACC-0002")
                .holderName("Bob")
                .currency("BRL")
                .balance(BigDecimal.valueOf(200))
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
        AccountEntity saved = accountJpaRepository.saveAndFlush(entity);

        Optional<AccountEntity> found = accountJpaRepository.findByIdForUpdate(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }
}
