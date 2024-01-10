package com.pgms.apievent.eventSeatArea.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.List;

public record EventSeatAreaCreateRequest(
	String seatAreaType,
	@Min(0) @Max(1000000000)
	Integer price,
	List<EventSeatAreaCreateRequest> requests) {
}
