package com.pgms.apibooking.factory;

import com.pgms.coredomain.domain.event.EventHall;

public class EventHallFactory {

	public static EventHall generate() {
		return EventHall.builder()
			.name("고척스카이돔")
			.address("서울 구로구 경인로 430")
			.build();
	}
}
