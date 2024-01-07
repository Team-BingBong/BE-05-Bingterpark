package com.pgms.apievent.eventSeatArea.dto.request;

import java.util.List;

import com.pgms.coredomain.domain.event.SeatAreaType;

public record EventSeatAreaCreateRequest(SeatAreaType seatAreaType,
										 Integer price,
										 List<EventSeatAreaCreateRequest> requests) {
}
