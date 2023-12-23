package com.pgms.apipayment.dto.response;

import java.util.List;

import com.pgms.coredomain.domain.event.EventSeat;

public record AreaResponse(
	String type,
	int price,
	int availableSeatCount,
	List<SeatResponse> seats
) {

	public static AreaResponse from(List<EventSeat> seats) {
		return new AreaResponse(
			seats.get(0).getEventSeatArea().getSeatAreaType().name(),
			seats.get(0).getEventSeatArea().getPrice(),
			seats.get(0).getEventSeatArea().getAvailableSeatCount(),
			seats.stream().map(SeatResponse::from).toList()
		);
	}
}
