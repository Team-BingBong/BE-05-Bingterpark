package com.pgms.apipayment.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.NoSuchElementException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.pgms.apipayment.config.TossPaymentConfig;
import com.pgms.apipayment.dto.request.PaymentConfirmRequest;
import com.pgms.apipayment.dto.request.PaymentCreateRequest;
import com.pgms.apipayment.dto.response.PaymentCreateResponse;
import com.pgms.apipayment.dto.response.PaymentSuccessResponse;
import com.pgms.coredomain.domain.order.Order;
import com.pgms.coredomain.domain.order.Payment;
import com.pgms.coredomain.domain.order.repository.OrderRepository;
import com.pgms.coredomain.domain.order.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final TossPaymentConfig tossPaymentConfig;

	public PaymentCreateResponse createPayment(PaymentCreateRequest request) {
		// TODO: order 생성 로직 추가하기 
		Order order = orderRepository.findById(request.orderId())
			.orElseThrow(() -> new NoSuchElementException("Order not found"));
		Payment payment = paymentRepository.save(request.toEntity(order));
		return PaymentCreateResponse.of(payment, tossPaymentConfig.getSuccessUrl(), tossPaymentConfig.getFailUrl());
	}

	public PaymentSuccessResponse succeedPayment(String paymentKey, String orderId, int amount) {
		Payment payment = getAndVerifyPayment(orderId, amount);
		PaymentSuccessResponse response = requestPaymentConfirmation(paymentKey, orderId, amount);

		switch (payment.getMethod()) {
			case CARD -> payment.updateCardInfo(response.card().number(), response.card().installmentPlanMonths(), response.card().isInterestFree());
			case VIRTUAL_ACCOUNT -> System.out.println("아직 안함");
			default -> throw new IllegalArgumentException("결제 수단이 올바르지 않습니다.");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
		payment.updateConfirmInfo(paymentKey, LocalDateTime.parse(response.approvedAt(), formatter), LocalDateTime.parse(response.requestedAt(), formatter));
		return response;
	}

	public PaymentSuccessResponse requestPaymentConfirmation(String paymentKey, String orderId, int amount) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = buildTossApiHeaders();
		PaymentConfirmRequest request = new PaymentConfirmRequest(paymentKey, orderId, amount);

		try { // tossPayments post 요청 (url , HTTP 객체 ,응답 Dto)
			ResponseEntity<PaymentSuccessResponse> response = restTemplate.postForEntity(
				TossPaymentConfig.URL, new HttpEntity<>(request, headers), PaymentSuccessResponse.class);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new RuntimeException("Toss Payments API 오류", e);
		} catch (HttpServerErrorException e) {
			throw new RuntimeException("Toss Payments API 서버 오류", e);
		} catch (Exception e) {
			throw new RuntimeException("Toss Payments 결제 승인 요청 오류", e);
		}
	}

	private Payment getAndVerifyPayment(String orderId, int amount) {
		// TODO: orderId String으로 변경시 수정 필요.
		Payment payment = paymentRepository.findByOrderId(1L).orElseThrow(() -> {
			throw new NoSuchElementException("해당 주문에 대한 payment 정보가 없습니다.");
		});
		if (payment.getAmount() != amount) {
			throw new IllegalArgumentException("결제 가격 정보가 일치하지 않습니다.");
		}
		return payment;
	}

	private HttpHeaders buildTossApiHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String encodedAuthKey = new String(
			Base64.getEncoder().encode((tossPaymentConfig.getTestSecretApiKey() + ":").getBytes(StandardCharsets.UTF_8)));
		headers.setBasicAuth(encodedAuthKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}
}
