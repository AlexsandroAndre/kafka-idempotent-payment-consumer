package com.alexsandroandre.kafka_idempotent_payment_consumer.application.service;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.PaymentResult;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentCommand;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentUseCase;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.AccountRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.FinancialTransactionRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.ProcessedEventRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.exception.AccountNotFoundException;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.Account;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.FinancialTransaction;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.ProcessedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProcessPaymentService implements ProcessPaymentUseCase {

    private final ProcessedEventRepositoryPort processedEventRepositoryPort;
    private final AccountRepositoryPort accountRepositoryPort;
    private final FinancialTransactionRepositoryPort financialTransactionRepositoryPort;

    public ProcessPaymentService(ProcessedEventRepositoryPort processedEventRepositoryPort,
                                  AccountRepositoryPort accountRepositoryPort,
                                  FinancialTransactionRepositoryPort financialTransactionRepositoryPort) {
        this.processedEventRepositoryPort = processedEventRepositoryPort;
        this.accountRepositoryPort = accountRepositoryPort;
        this.financialTransactionRepositoryPort = financialTransactionRepositoryPort;
    }

    @Override
    @Transactional
    public PaymentResult process(ProcessPaymentCommand command) {
        ProcessedEvent claim = ProcessedEvent.claim(
                command.idempotencyKey(),
                command.eventId(),
                command.transactionId(),
                command.payloadHash());

        int claimed = processedEventRepositoryPort.claim(claim);
        if (claimed == 0) {
            return PaymentResult.duplicate(command.idempotencyKey());
        }

        Account account = accountRepositoryPort.findByIdForUpdate(command.accountId())
                .orElseThrow(() -> new AccountNotFoundException(command.accountId().toString()));

        account.credit(command.amount(), command.currency());
        accountRepositoryPort.save(account);

        FinancialTransaction financialTransaction = FinancialTransaction.credit(
                command.transactionId(),
                command.accountId(),
                command.amount(),
                command.currency());
        financialTransactionRepositoryPort.save(financialTransaction);

        return PaymentResult.processed(command.transactionId());
    }
}
