package com.pgms.apibooking.factory;

import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;

public class PaymentFactory {

	public static Payment generate(PaymentMethod method, Integer amount, PaymentStatus status) {
		return Payment.builder()
			.method(method)
			.amount(amount)
			.status(status)
			.build();
	}
}
