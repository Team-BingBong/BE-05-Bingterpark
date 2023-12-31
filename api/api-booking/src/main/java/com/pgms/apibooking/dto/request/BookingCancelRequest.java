package com.pgms.apibooking.dto.request;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingCancel;

import jakarta.validation.constraints.NotBlank;

public record BookingCancelRequest(@NotBlank String reason) {

	public static BookingCancel toEntity(BookingCancelRequest request, String createdBy, Booking booking) {
		return BookingCancel.builder()
			.reason(request.reason())
			.amount(booking.getAmount())
			.createdBy(createdBy)
			.booking(booking)
			.build();
	}
}
