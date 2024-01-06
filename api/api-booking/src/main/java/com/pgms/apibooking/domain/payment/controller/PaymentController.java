package com.pgms.apibooking.domain.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apibooking.domain.payment.dto.request.ConfirmVirtualIncomeRequest;
import com.pgms.apibooking.domain.payment.dto.response.PaymentFailResponse;
import com.pgms.apibooking.domain.payment.service.PaymentService;
import com.pgms.coredomain.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;

	@GetMapping("/success")
	public ResponseEntity<ApiResponse> confirmPaymentSuccess(
		@RequestParam String paymentKey,
		@RequestParam(name = "orderId") String bookingId,
		@RequestParam int amount
	) {
		return ResponseEntity.ok(ApiResponse.ok(paymentService.successPayment(paymentKey, bookingId, amount)));
	}

	@GetMapping("/fail")
	public ResponseEntity<ApiResponse> confirmPaymentFail(
		@RequestParam(name = "code") String errorCode,
		@RequestParam(name = "message") String errorMessage,
		@RequestParam(name = "orderId") String orderId
	) {
		ApiResponse<PaymentFailResponse> response = ApiResponse.ok(
			paymentService.failPayment(errorCode, errorMessage, orderId));
		return ResponseEntity.ok(response);
	}

	@PostMapping("/virtual/income")
	public ResponseEntity<Void> confirmVirtualAccountIncome(@RequestBody ConfirmVirtualIncomeRequest request) {
		System.out.println(request.createdAt());
		paymentService.confirmVirtualAccountIncome(request);
		return ResponseEntity.ok().build();
	}
}
