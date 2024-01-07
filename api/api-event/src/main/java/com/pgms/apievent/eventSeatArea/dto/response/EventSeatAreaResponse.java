package com.pgms.apievent.eventSeatArea.dto.response;

import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.SeatAreaType;

public record EventSeatAreaResponse(Long id,
									SeatAreaType seatAreaType,
									int price) {
	public static EventSeatAreaResponse of(EventSeatArea eventSeatArea) {
		return new EventSeatAreaResponse(
			eventSeatArea.getId(),
			eventSeatArea.getSeatAreaType(),
			eventSeatArea.getPrice()
		);
	}
}
