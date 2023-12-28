package com.pgms.apievent.eventSeat.dto;

import com.pgms.coredomain.domain.event.EventSeatArea;

public record LeftEventSeatNumDto(EventSeatArea eventSeatArea,
                                  Integer leftSeatNumber) {
}
