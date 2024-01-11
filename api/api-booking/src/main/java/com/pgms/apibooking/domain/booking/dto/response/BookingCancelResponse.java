package com.pgms.apibooking.domain.booking.dto.response;

import com.pgms.coredomain.domain.booking.BookingCancel;

public record BookingCancelResponse(
	Long id,
	Integer amount,
	String reason,
	String createdBy,
	String createdAt
) {

	public static BookingCancelResponse from(BookingCancel cancel) {
		return new BookingCancelResponse(
			cancel.getId(),
			cancel.getAmount(),
			cancel.getReason(),
			cancel.getCreatedBy(),
			cancel.getCreatedAt().toString()
		);
	}
}
