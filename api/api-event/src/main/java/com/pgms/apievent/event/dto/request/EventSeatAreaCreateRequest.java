package com.pgms.apievent.event.dto.request;

import com.pgms.coredomain.domain.event.SeatAreaType;

import java.util.List;

public record EventSeatAreaCreateRequest(SeatAreaType seatAreaType,
                                         Integer price,
                                         List<EventSeatAreaCreateRequest> requests) {
}
