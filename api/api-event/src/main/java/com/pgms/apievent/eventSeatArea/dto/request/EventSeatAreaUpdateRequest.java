package com.pgms.apievent.eventSeatArea.dto.request;

import com.pgms.coredomain.domain.event.SeatAreaType;

public record EventSeatAreaUpdateRequest(SeatAreaType seatAreaType,
										 Integer price) {
}
