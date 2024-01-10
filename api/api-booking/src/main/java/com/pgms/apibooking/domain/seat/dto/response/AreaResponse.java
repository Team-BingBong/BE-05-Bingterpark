package com.pgms.apibooking.domain.seat.dto.response;

import com.pgms.coredomain.domain.event.EventSeat;

import java.util.List;

public record AreaResponse(
	Long id,
	String type,
	int price,
//	int availableSeatCount,
	List<SeatResponse> seats
) {

	public static AreaResponse from(List<EventSeat> seats) {
		return new AreaResponse(
			seats.get(0).getEventSeatArea().getId(),
			seats.get(0).getEventSeatArea().getSeatAreaType().getDescription(),
			seats.get(0).getEventSeatArea().getPrice(),
//			seats.get(0).getEventSeatArea().getAvailableSeatCount(),
			seats.stream().map(SeatResponse::from).toList()
		);
	}
}
