package com.pgms.apipayment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class TossPaymentConfig {

	@Value("${payment.toss_test_client_api_key}")
	private String testClientApiKey;

	@Value("${payment.toss_test_secret_api_key}")
	private String testSecretApiKey;

	@Value("${payment.success_url}")
	private String successUrl;

	@Value("${payment.fail_url}")
	private String failUrl;

	public static final String URL = "https://api.tosspayments.com/v1/payments/confirm";
}
