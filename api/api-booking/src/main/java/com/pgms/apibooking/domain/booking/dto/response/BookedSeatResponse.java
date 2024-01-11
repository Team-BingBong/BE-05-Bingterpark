package com.pgms.apibooking.domain.booking.dto.response;

import com.pgms.coredomain.domain.event.EventSeat;

public record BookedSeatResponse(
	String areaType,
	String name,
	Integer price
) {

	public static BookedSeatResponse from(EventSeat seat) {
		return new BookedSeatResponse(
			seat.getEventSeatArea().getSeatAreaType().getDescription(),
			seat.getName(),
			seat.getEventSeatArea().getPrice()
		);
	}
}
