package com.alexsandroandre.kafka_idempotent_payment_consumer.application.service;

import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.PaymentResult;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in.ProcessPaymentCommand;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.AccountRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.FinancialTransactionRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.out.ProcessedEventRepositoryPort;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.exception.AccountNotFoundException;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.Account;
import com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model.FinancialTransaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentServiceTest {

    @Mock
    private ProcessedEventRepositoryPort processedEventRepositoryPort;

    @Mock
    private AccountRepositoryPort accountRepositoryPort;

    @Mock
    private FinancialTransactionRepositoryPort financialTransactionRepositoryPort;

    private ProcessPaymentService processPaymentService;

    private static final UUID ACCOUNT_ID = UUID.randomUUID();

    private ProcessPaymentCommand aCommand() {
        return new ProcessPaymentCommand(
                "idem-key-1",
                "event-1",
                "txn-1",
                ACCOUNT_ID,
                BigDecimal.valueOf(100),
                "BRL",
                "hash-1");
    }

    private Account anAccount() {
        return new Account(
                ACCOUNT_ID,
                "ACC-0001",
                "Alice",
                "BRL",
                BigDecimal.valueOf(500),
                OffsetDateTime.now(),
                OffsetDateTime.now());
    }

    private void newService() {
        processPaymentService = new ProcessPaymentService(
                processedEventRepositoryPort, accountRepositoryPort, financialTransactionRepositoryPort);
    }

    @Test
    void shouldProcessPaymentWhenEventIsNew() {
        when(processedEventRepositoryPort.claim(any())).thenReturn(1);
        when(accountRepositoryPort.findByIdForUpdate(ACCOUNT_ID)).thenReturn(Optional.of(anAccount()));
        when(accountRepositoryPort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        newService();

        PaymentResult result = processPaymentService.process(aCommand());

        assertThat(result.status()).isEqualTo(PaymentResult.PaymentStatus.PROCESSED);
        assertThat(result.reference()).isEqualTo("txn-1");

        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepositoryPort).save(accountCaptor.capture());
        assertThat(accountCaptor.getValue().getBalance()).isEqualByComparingTo(BigDecimal.valueOf(600));

        ArgumentCaptor<FinancialTransaction> ledgerCaptor = ArgumentCaptor.forClass(FinancialTransaction.class);
        verify(financialTransactionRepositoryPort).save(ledgerCaptor.capture());
        assertThat(ledgerCaptor.getValue().getTransactionId()).isEqualTo("txn-1");
        assertThat(ledgerCaptor.getValue().getAmount()).isEqualByComparingTo(BigDecimal.valueOf(100));
    }

    @Test
    void shouldReturnDuplicateWithoutTouchingAccountWhenEventAlreadyClaimed() {
        when(processedEventRepositoryPort.claim(any())).thenReturn(0);
        newService();

        PaymentResult result = processPaymentService.process(aCommand());

        assertThat(result.status()).isEqualTo(PaymentResult.PaymentStatus.DUPLICATE);
        assertThat(result.reference()).isEqualTo("idem-key-1");
        verify(accountRepositoryPort, never()).findByIdForUpdate(any());
        verify(accountRepositoryPort, never()).save(any());
        verify(financialTransactionRepositoryPort, never()).save(any());
    }

    @Test
    void shouldThrowWhenAccountDoesNotExist() {
        when(processedEventRepositoryPort.claim(any())).thenReturn(1);
        when(accountRepositoryPort.findByIdForUpdate(ACCOUNT_ID)).thenReturn(Optional.empty());
        newService();

        assertThatThrownBy(() -> processPaymentService.process(aCommand()))
                .isInstanceOf(AccountNotFoundException.class);

        verify(financialTransactionRepositoryPort, never()).save(any());
    }

    @Test
    void shouldPropagateExceptionWhenCurrencyMismatches() {
        when(processedEventRepositoryPort.claim(any())).thenReturn(1);
        when(accountRepositoryPort.findByIdForUpdate(ACCOUNT_ID)).thenReturn(Optional.of(anAccount()));
        newService();

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                "idem-key-1", "event-1", "txn-1", ACCOUNT_ID, BigDecimal.valueOf(100), "USD", "hash-1");

        assertThatThrownBy(() -> processPaymentService.process(command))
                .isInstanceOf(IllegalArgumentException.class);

        verify(accountRepositoryPort, never()).save(any());
        verify(financialTransactionRepositoryPort, never()).save(any());
    }
}
