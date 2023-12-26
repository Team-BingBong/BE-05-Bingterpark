package com.pgms.apievent.factory.eventhall;

import java.util.ArrayList;

import com.pgms.coredomain.domain.event.EventHall;

public class EventHallFactory {

	public static EventHall createEventHall() {
		return EventHall.builder()
			.name("예술공간서울")
			.address("서울특별시 종로구 명륜2가 성균관로4길 19")
			.eventHallSeats(new ArrayList<>())
			.build();
	}
}
