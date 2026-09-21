package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.Account;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getAccountNumber(),
                entity.getHolderName(),
                entity.getCurrency(),
                entity.getBalance(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public AccountEntity toEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .holderName(account.getHolderName())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}
