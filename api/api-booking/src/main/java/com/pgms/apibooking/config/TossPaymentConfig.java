package com.pgms.apibooking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Getter
@Configuration
public class TossPaymentConfig {

	public static final String TOSS_CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";
	public static final String TOSS_ORIGIN_URL = "https://api.tosspayments.com/v1/payments/";

	@Value("${payment.toss.test-client-api-key}")
	private String testClientApiKey;

	@Value("${payment.toss.test-secret-api-key}")
	private String testSecretApiKey;

	@Value("${payment.toss.success-url}")
	private String successUrl;

	@Value("${payment.toss.fail-url}")
	private String failUrl;
}
