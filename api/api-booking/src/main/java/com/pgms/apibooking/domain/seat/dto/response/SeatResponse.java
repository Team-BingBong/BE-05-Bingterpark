package com.pgms.apibooking.domain.seat.dto.response;

import com.pgms.coredomain.domain.event.EventSeat;

public record SeatResponse(
	String name,
	boolean isSelectable
) {

	public static SeatResponse from(EventSeat seat) {
		return new SeatResponse(
			seat.getName(),
			!seat.isBooked()
		);
	}
}
