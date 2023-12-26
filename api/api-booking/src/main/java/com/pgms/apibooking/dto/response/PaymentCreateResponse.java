package com.pgms.apibooking.dto.response;

import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;

public record PaymentCreateResponse(
	PaymentMethod paymentMethod,
	int amount,
	String status,
	String successUrl,
	String failUrl
) {
	public static PaymentCreateResponse of(Payment payment, String successUrl, String failUrl) {
		return new PaymentCreateResponse(
			payment.getMethod(),
			payment.getAmount(),
			payment.getStatus().toString(),
			successUrl,
			failUrl
		);
	}
}