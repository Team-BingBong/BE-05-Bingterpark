package com.pgms.apievent.eventSeatArea.dto.request;

import java.util.List;

public record EventSeatAreaCreateRequest(
	String seatAreaType,
	Integer price,
	List<EventSeatAreaCreateRequest> requests) {
}
