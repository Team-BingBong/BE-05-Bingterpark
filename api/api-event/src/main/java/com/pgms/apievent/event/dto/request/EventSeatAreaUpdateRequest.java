package com.pgms.apievent.event.dto.request;

import com.pgms.coredomain.domain.event.SeatAreaType;

public record EventSeatAreaUpdateRequest(SeatAreaType seatAreaType,
                                         Integer price) {
}
