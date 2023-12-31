package com.pgms.apibooking.dto.response;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.PaymentMethod;

public record BookingCreateResponse(
	String bookingId,
	int amount,
	String status,
	PaymentMethod paymentMethod,
	String paymentSuccessUrl,
	String paymentFailUrl
) {

	public static BookingCreateResponse of(Booking booking, String paymentSuccessUrl, String paymentFailUrl) {
		return new BookingCreateResponse(
			booking.getId(),
			booking.getAmount(),
			booking.getStatus().name(),
			booking.getPayment().getMethod(),
			paymentSuccessUrl,
			paymentFailUrl
		);
	}
}
