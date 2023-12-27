package com.pgms.apibooking.dto.response;

import com.pgms.coredomain.domain.event.EventSeat;

import java.util.List;

public record AreaResponse(
	String type,
	int price,
//	int availableSeatCount,
	List<SeatResponse> seats
) {

	public static AreaResponse from(List<EventSeat> seats) {
		return new AreaResponse(
			seats.get(0).getEventSeatArea().getSeatAreaType().getDescription(),
			seats.get(0).getEventSeatArea().getPrice(),
//			seats.get(0).getEventSeatArea().getAvailableSeatCount(),
			seats.stream().map(SeatResponse::from).toList()
		);
	}
}
