package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.Account;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.AccountEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountMapperTest {

    public static final String ACCOUNT_NUMBER = "ACC-100";
    public static final String HOLDER_NAME = "Alice";
    public static final String CURRENCY = "BRL";
    public static final String EXPECTED = "BRL";
    public static final String ACC_200 = "ACC-200";
    public static final String BOB = "Bob";
    public static final String USD = "USD";
    private final AccountMapper accountMapper = new AccountMapper();

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = createdAt.plusMinutes(5);
        Account account = new Account(
                id,
                ACCOUNT_NUMBER,
                HOLDER_NAME,
                CURRENCY,
                BigDecimal.valueOf(1500),
                createdAt,
                updatedAt);

        AccountEntity entity = accountMapper.toEntity(account);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getAccountNumber()).isEqualTo(ACCOUNT_NUMBER);
        assertThat(entity.getHolderName()).isEqualTo(HOLDER_NAME);
        assertThat(entity.getCurrency()).isEqualTo(EXPECTED);
        assertThat(entity.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(1500));
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = createdAt.plusMinutes(10);
        AccountEntity entity = AccountEntity.builder()
                .id(id)
                .accountNumber(ACC_200)
                .holderName(BOB)
                .currency(USD)
                .balance(BigDecimal.valueOf(2300))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        Account account = accountMapper.toDomain(entity);

        assertThat(account.getId()).isEqualTo(id);
        assertThat(account.getAccountNumber()).isEqualTo(ACC_200);
        assertThat(account.getHolderName()).isEqualTo(BOB);
        assertThat(account.getCurrency()).isEqualTo(USD);
        assertThat(account.getBalance()).isEqualByComparingTo(BigDecimal.valueOf(2300));
        assertThat(account.getCreatedAt()).isEqualTo(createdAt);
        assertThat(account.getUpdatedAt()).isEqualTo(updatedAt);
    }
}
