package com.pgms.apievent.eventSeat.dto.response;

import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventSeatStatus;

public record EventSeatResponse(Long id,
                                String name,
                                EventSeatStatus status,
                                EventSeatArea eventSeatArea) {
    public static EventSeatResponse of(EventSeat eventSeat){
        return new EventSeatResponse(eventSeat.getId(),
                eventSeat.getName(),
                eventSeat.getStatus(),
                eventSeat.getEventSeatArea());
    }
}
