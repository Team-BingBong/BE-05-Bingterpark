package com.pgms.apibooking.domain.booking.dto.response;

import java.time.LocalDateTime;

public record BookingCancelResponse(
	String reason,
	Integer amount,
	LocalDateTime createdAt
) {
}
