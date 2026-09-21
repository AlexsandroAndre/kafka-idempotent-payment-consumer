package com.alexsandroandre.kafka_idempotent_payment_consumer.application.port.in;

public interface ProcessPaymentUseCase {

    PaymentResult process(ProcessPaymentCommand command);
}
