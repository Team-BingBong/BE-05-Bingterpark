package com.pgms.apibooking.domain.payment.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.common.exception.TossPaymentException;
import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.domain.payment.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.domain.payment.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentSuccessResponse;
import com.pgms.coredomain.domain.common.BookingErrorCode;
import com.pgms.coredomain.response.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TossPaymentServiceImpl implements TossPaymentService {

	private final TossPaymentConfig tossPaymentConfig;
	private final RestTemplate restTemplate;

	@Override
	public PaymentSuccessResponse requestTossPaymentConfirmation(PaymentConfirmRequest request) {
		HttpHeaders headers = buildTossApiHeaders();
		try { // tossPayments post 요청 (url , HTTP 객체 ,응답 Dto)
			return restTemplate.postForObject(
				TossPaymentConfig.TOSS_CONFIRM_URL, new HttpEntity<>(request, headers), PaymentSuccessResponse.class);
		} catch (HttpClientErrorException e) {
			log.warn("HttpClientErrorException: {}", e.getMessage());
			ErrorResponse errorResponse = handleHttpClientErrorException(e.getResponseBodyAsString());
			HttpStatus status = (HttpStatus)e.getStatusCode();
			throw new TossPaymentException(errorResponse, status);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public PaymentCancelResponse requestTossPaymentCancellation(String paymentKey, PaymentCancelRequest request) {
		HttpHeaders headers = buildTossApiHeaders();
		URI uri = URI.create(TossPaymentConfig.TOSS_ORIGIN_URL + paymentKey + "/cancel");
		try {
			return restTemplate.postForObject(
				uri, new HttpEntity<>(request, headers), PaymentCancelResponse.class);
		} catch (HttpClientErrorException e) {
			ErrorResponse errorResponse = handleHttpClientErrorException(e.getResponseBodyAsString());
			HttpStatus status = (HttpStatus)e.getStatusCode();
			throw new TossPaymentException(errorResponse, status);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private HttpHeaders buildTossApiHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String encodedAuthKey = new String(
			Base64.getEncoder()
				.encode((tossPaymentConfig.getTestSecretApiKey() + ":").getBytes(StandardCharsets.UTF_8)));
		headers.setBasicAuth(encodedAuthKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private ErrorResponse handleHttpClientErrorException(String body) {
		if (body == null | body.isBlank()) {
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(body);
			String code = jsonNode.get("code").asText();
			String message = jsonNode.get("message").asText();
			return new ErrorResponse(code, message);
		} catch (JsonProcessingException e) {
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
