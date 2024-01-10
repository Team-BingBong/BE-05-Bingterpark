package com.pgms.apibooking.domain.seat.dto.response;

import com.pgms.coredomain.domain.event.EventSeat;

public record SeatResponse(
	Long id,
	String name,
	boolean isSelectable
) {

	public static SeatResponse from(EventSeat seat) {
		return new SeatResponse(
			seat.getId(),
			seat.getName(),
			!seat.isBooked()
		);
	}
}
