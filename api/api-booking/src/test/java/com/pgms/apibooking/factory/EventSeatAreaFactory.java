package com.pgms.apibooking.factory;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.SeatAreaType;

public class EventSeatAreaFactory {

	public static EventSeatArea generate(Event event, SeatAreaType type) {
		return EventSeatArea.builder()
			.seatAreaType(type)
			.price(10000)
			.event(event)
			.build();
	}
}
