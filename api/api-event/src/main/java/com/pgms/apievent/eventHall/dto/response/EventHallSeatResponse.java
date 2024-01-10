package com.pgms.apievent.eventHall.dto.response;

import com.pgms.coredomain.domain.event.EventHallSeat;

import lombok.Builder;

@Builder
public record EventHallSeatResponse(Long id, String name) {

	public static EventHallSeatResponse of(EventHallSeat eventHallSeat) {
		return EventHallSeatResponse.builder()
			.id(eventHallSeat.getId())
			.name(eventHallSeat.getName())
			.build();
	}
}
