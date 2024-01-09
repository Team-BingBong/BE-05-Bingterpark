package com.pgms.apibooking.domain.booking.dto.response;

import com.pgms.coredomain.domain.booking.Booking;

public record BookingsGetResponse(
	String id,
	String bookingName,
	Integer amount,
	String status,
	String eventTimeStartedAt,
	String eventTimeEndedAt,
	String createdAt
) {

	public static BookingsGetResponse from(Booking booking) {
		return new BookingsGetResponse(
			booking.getId(),
			booking.getBookingName(),
			booking.getAmount(),
			booking.getStatus().getDescription(),
			booking.getTime().getStartedAt().toString(),
			booking.getTime().getEndedAt().toString(),
			booking.getCreatedAt().toString()
		);
	}
}
