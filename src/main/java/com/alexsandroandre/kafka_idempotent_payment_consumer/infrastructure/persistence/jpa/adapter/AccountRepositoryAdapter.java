package com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.adapter;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.AccountRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.Account;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.entity.AccountEntity;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.mapper.AccountMapper;
import com.alexsandroandre.kafka_idempotent_payment_consumer.infrastructure.persistence.jpa.repository.AccountJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class AccountRepositoryAdapter implements AccountRepositoryPort {

    private final AccountJpaRepository accountJpaRepository;
    private final AccountMapper accountMapper;

    public AccountRepositoryAdapter(AccountJpaRepository accountJpaRepository, AccountMapper accountMapper) {
        this.accountJpaRepository = accountJpaRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<Account> findByIdForUpdate(UUID accountId) {
        return accountJpaRepository.findByIdForUpdate(accountId).map(accountMapper::toDomain);
    }

    @Override
    public Account save(Account account) {
        AccountEntity saved = accountJpaRepository.save(accountMapper.toEntity(account));
        return accountMapper.toDomain(saved);
    }
}
