package com.pgms.apibooking.dto.response;

import com.pgms.coredomain.domain.event.EventSeat;

public record SeatResponse(
	String name,
	boolean isAvailable
) {

	public static SeatResponse from(EventSeat seat) {
		return new SeatResponse(
			seat.getName(),
			seat.isAvailable()
		);
	}
}
