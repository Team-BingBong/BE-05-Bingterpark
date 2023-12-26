package com.pgms.apibooking.dto.request;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;

public record PaymentCreateRequest(
	PaymentMethod method,
	Long bookingId,
	int amount
) {
	public Payment toEntity(Booking booking) {
		return Payment.builder()
			.method(method)
			.booking(booking)
			.amount(amount)
			.status(PaymentStatus.WAITING_FOR_DEPOSIT)
			.build();
	}
}
