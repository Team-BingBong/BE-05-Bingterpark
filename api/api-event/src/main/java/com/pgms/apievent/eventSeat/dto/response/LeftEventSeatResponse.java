package com.pgms.apievent.eventSeat.dto.response;

import com.pgms.coredomain.domain.event.SeatAreaType;

public record LeftEventSeatResponse(SeatAreaType seatAreaType,
                                    Long leftSeatNumber) {
}
