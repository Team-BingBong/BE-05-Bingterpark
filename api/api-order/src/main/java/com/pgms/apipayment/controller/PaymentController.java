package com.pgms.apipayment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apipayment.dto.request.PaymentCreateRequest;
import com.pgms.apipayment.dto.response.PaymentCreateResponse;
import com.pgms.apipayment.service.PaymentService;
import com.pgms.coredomain.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping
	public ResponseEntity<ApiResponse> createPayment(@RequestBody PaymentCreateRequest request) {
		ApiResponse<PaymentCreateResponse> response = ApiResponse.ok(paymentService.createPayment(request));
		return ResponseEntity.ok(response);
	}
}
