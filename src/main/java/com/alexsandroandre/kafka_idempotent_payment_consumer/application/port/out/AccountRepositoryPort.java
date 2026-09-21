package com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepositoryPort {

    Optional<Account> findByIdForUpdate(UUID accountId);

    Account save(Account account);
}
