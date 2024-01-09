package com.pgms.apievent.eventSeat.dto.response;

import com.pgms.apievent.eventSeatArea.dto.response.EventSeatAreaResponse;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;

public record EventSeatResponse(Long id,
                                String name,
                                EventSeatStatus status,
                                EventSeatAreaResponse eventSeatAreaResponse) {
    public static EventSeatResponse of(EventSeat eventSeat){
        return new EventSeatResponse(eventSeat.getId(),
                eventSeat.getName(),
                eventSeat.getStatus(),
                EventSeatAreaResponse.of(eventSeat.getEventSeatArea()));
    }
}
