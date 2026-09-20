package com.alexsandroandre.kafka_idempotent_payment_consumer;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class KafkaIdempotentPaymentConsumerApplicationTests {

	public static final String PAYMENT_DB = "payment_db";


	public static final String PAYMENT_USER = "payment_user";
	public static final String PAYMENT_PASS = "payment_pass";
	public static final String POSTGRES_16_ALPINE = "postgres:16-alpine";
	public static final String SPRING_DATASOURCE_URL = "spring.datasource.url";
	public static final String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
	public static final String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";

	@Container
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_16_ALPINE)
			.withDatabaseName(PAYMENT_DB)
			.withUsername(PAYMENT_USER)
			.withPassword(PAYMENT_PASS);

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add(SPRING_DATASOURCE_URL, postgres::getJdbcUrl);
		registry.add(SPRING_DATASOURCE_USERNAME, postgres::getUsername);
		registry.add(SPRING_DATASOURCE_PASSWORD, postgres::getPassword);
	}

	@Test
	void contextLoads() {
	}

}
