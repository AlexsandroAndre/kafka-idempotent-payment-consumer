package com.alexsandroandre.kafka_idempotent_payment_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaIdempotentPaymentConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaIdempotentPaymentConsumerApplication.class, args);
	}

}
