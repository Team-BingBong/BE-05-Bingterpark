package com.pgms.apibooking.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;

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
			throw new BookingException(BookingErrorCode.TOSS_PAYMENTS_ERROR);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public PaymentCancelResponse requestTossPaymentCancellation(String paymentKey, BookingCancelRequest request) {
		HttpHeaders headers = buildTossApiHeaders();
		URI uri = URI.create(TossPaymentConfig.TOSS_ORIGIN_URL + paymentKey + "/cancel");
		try {
			return restTemplate.postForObject(
				uri, new HttpEntity<>(request, headers), PaymentCancelResponse.class);
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
}
