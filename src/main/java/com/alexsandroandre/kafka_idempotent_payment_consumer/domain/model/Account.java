package com.alexsandroandre.kafka_idempotent_payment_consumer.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Account {

    public static final String CREDIT_AMOUNT_MUST_BE_POSITIVE = "Credit amount must be positive";
    public static final String CURRENCY_MISMATCH_ACCOUNT = "Currency mismatch: account=";

    public static final String PAYMENT = " payment=";
    private final UUID id;
    private final String accountNumber;
    private final String holderName;
    private final String currency;
    private BigDecimal balance;
    private final OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Account(UUID id,
                   String accountNumber,
                   String holderName,
                   String currency,
                   BigDecimal balance,
                   OffsetDateTime createdAt,
                   OffsetDateTime updatedAt) {
        
        this.id = Objects.requireNonNull(id);
        this.accountNumber = Objects.requireNonNull(accountNumber);
        this.holderName = Objects.requireNonNull(holderName);
        this.currency = Objects.requireNonNull(currency);
        this.balance = Objects.requireNonNull(balance);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void credit(BigDecimal amount, String paymentCurrency) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(CREDIT_AMOUNT_MUST_BE_POSITIVE);
        }
        if (!currency.equalsIgnoreCase(paymentCurrency)) {
            throw new IllegalArgumentException(CURRENCY_MISMATCH_ACCOUNT + currency + PAYMENT + paymentCurrency);
        }
        balance = balance.add(amount);
        updatedAt = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public String getHolderName() { return holderName; }
    public String getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
