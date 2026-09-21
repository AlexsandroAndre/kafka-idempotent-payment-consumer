package com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out;

import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.FinancialTransaction;

public interface FinancialTransactionRepositoryPort {

    FinancialTransaction save(FinancialTransaction financialTransaction);
}
