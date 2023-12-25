package com.pgms.apipayment.dto.response;

import com.pgms.coredomain.domain.event.EventSeat;

public record SeatResponse(
	long id,
	String name,
	boolean isAvailable
) {

	public static SeatResponse from(EventSeat seat) {
		return new SeatResponse(
			seat.getId(),
			seat.getName(),
			seat.isAvailable()
		);
	}
}
