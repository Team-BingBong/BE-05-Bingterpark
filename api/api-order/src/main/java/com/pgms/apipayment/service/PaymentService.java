package com.pgms.apipayment.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apipayment.dto.request.PaymentCreateRequest;
import com.pgms.apipayment.dto.response.PaymentCreateResponse;
import com.pgms.coredomain.domain.order.Order;
import com.pgms.coredomain.domain.order.repository.OrderRepository;
import com.pgms.coredomain.domain.order.Payment;
import com.pgms.coredomain.domain.order.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	@Value("${payment.toss_test_client_api_key}")
	private String testClientApiKey;

	@Value("${payment.toss_test_secret_api_key}")
	private String testSecretApiKey;

	@Value("${payment.success_url}")
	private String successUrl;

	@Value("${payment.fail_url}")
	private String failUrl;
	public static final String URL = "https://api.tosspayments.com/v1/payments/confirm";

	public PaymentCreateResponse createPayment(PaymentCreateRequest request) {
		// TODO: order 생성 로직 추가하기 
		Order order = orderRepository.findById(request.orderId())
			.orElseThrow(() -> new NoSuchElementException("Order not found"));
		Payment payment = paymentRepository.save(request.toEntity(order));
		return PaymentCreateResponse.of(payment, successUrl, failUrl);
	}
}
