package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class FinancialTransaction {

    public static final String TRANSACTION_AMOUNT_MUST_BE_POSITIVE = "Transaction amount must be positive";
    
    private final UUID id;
    private final String transactionId;
    private final UUID accountId;
    private final BigDecimal amount;
    private final String currency;
    private final TransactionType transactionType;
    private final OffsetDateTime createdAt;

    private FinancialTransaction(UUID id,
                                 String transactionId,
                                 UUID accountId,
                                 BigDecimal amount,
                                 String currency,
                                 TransactionType transactionType,
                                 OffsetDateTime createdAt) {
        this.id = id;
        this.transactionId = Objects.requireNonNull(transactionId);
        this.accountId = Objects.requireNonNull(accountId);
        this.amount = Objects.requireNonNull(amount);
        this.currency = Objects.requireNonNull(currency);
        this.transactionType = Objects.requireNonNull(transactionType);
        this.createdAt = createdAt;
    }

    public static FinancialTransaction credit(String transactionId, UUID accountId,
                                              BigDecimal amount, String currency) {
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException(TRANSACTION_AMOUNT_MUST_BE_POSITIVE);
        }
        return new FinancialTransaction(
                UUID.randomUUID(),
                transactionId,
                accountId,
                amount,
                currency,
                TransactionType.CREDIT,
                null);
    }

    public UUID getId() { return id; }
    public String getTransactionId() { return transactionId; }
    public UUID getAccountId() { return accountId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public TransactionType getTransactionType() { return transactionType; }
    public OffsetDateTime getCreatedAt() { return createdAt; }

    public enum TransactionType { CREDIT }
}
