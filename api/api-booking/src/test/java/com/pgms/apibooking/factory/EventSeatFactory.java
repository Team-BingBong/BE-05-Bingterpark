package com.pgms.apibooking.factory;

import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;

public class EventSeatFactory {

	public static EventSeat generate(EventTime time, EventSeatArea area, String name) {
		return EventSeat.builder()
			.name(name)
			.status(EventSeatStatus.AVAILABLE)
			.eventTime(time)
			.eventSeatArea(area)
			.build();
	}
}
