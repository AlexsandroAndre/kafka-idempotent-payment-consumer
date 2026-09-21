package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountEntityTest {

    @Test
    void shouldBuildEntityWithBuilder() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        AccountEntity entity = AccountEntity.builder()
                .id(id)
                .accountNumber("ACC-0001")
                .holderName("Alice Nakamoto Corp")
                .currency("BRL")
                .balance(BigDecimal.TEN)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getAccountNumber()).isEqualTo("ACC-0001");
        assertThat(entity.getHolderName()).isEqualTo("Alice Nakamoto Corp");
        assertThat(entity.getCurrency()).isEqualTo("BRL");
        assertThat(entity.getBalance()).isEqualByComparingTo(BigDecimal.TEN);
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void shouldAllowFieldMutationViaSetters() {
        AccountEntity entity = new AccountEntity();

        entity.setId(UUID.randomUUID());
        entity.setAccountNumber("ACC-0002");
        entity.setHolderName("Bob");
        entity.setCurrency("USD");
        entity.setBalance(BigDecimal.ONE);

        assertThat(entity.getAccountNumber()).isEqualTo("ACC-0002");
        assertThat(entity.getHolderName()).isEqualTo("Bob");
        assertThat(entity.getCurrency()).isEqualTo("USD");
        assertThat(entity.getBalance()).isEqualByComparingTo(BigDecimal.ONE);
    }

    @Test
    void shouldCreateEntityWithAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        AccountEntity entity = new AccountEntity(id, "ACC-0003", "Carol", "BRL",
                BigDecimal.valueOf(50), now, now);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getAccountNumber()).isEqualTo("ACC-0003");
    }
}
