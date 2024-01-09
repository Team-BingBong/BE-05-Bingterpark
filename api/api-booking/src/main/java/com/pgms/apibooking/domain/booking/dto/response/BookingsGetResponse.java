package com.pgms.apibooking.domain.booking.dto.response;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.booking.Booking;

public record BookingsGetResponse(
	String id,
	String bookingName,
	String status,
	LocalDateTime eventTimeStartedAt,
	LocalDateTime eventTimeEndedAt,
	LocalDateTime createdAt
) {

	public static BookingsGetResponse from(Booking booking) {
		return new BookingsGetResponse(
			booking.getId(),
			booking.getBookingName(),
			booking.getStatus().getDescription(),
			booking.getTime().getStartedAt(),
			booking.getTime().getEndedAt(),
			booking.getCreatedAt()
		);
	}
}
