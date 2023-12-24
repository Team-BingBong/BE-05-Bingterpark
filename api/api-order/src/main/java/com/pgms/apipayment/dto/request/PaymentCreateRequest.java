package com.pgms.apipayment.dto.request;

import com.pgms.coredomain.domain.order.Order;
import com.pgms.coredomain.domain.order.Payment;
import com.pgms.coredomain.domain.order.PaymentMethod;
import com.pgms.coredomain.domain.order.PaymentStatus;

public record PaymentCreateRequest(
	PaymentMethod method,
	Long orderId,
	int amount
) {
	public Payment toEntity(Order order) {
		return Payment.builder()
			.method(method)
			.order(order)
			.amount(amount)
			.status(PaymentStatus.DEPOSIT_PENDING)
			.build();
	}
}
