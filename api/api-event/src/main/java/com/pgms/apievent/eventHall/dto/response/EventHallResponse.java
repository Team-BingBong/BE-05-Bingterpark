package com.pgms.apievent.eventHall.dto.response;

import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventHallSeat;
import lombok.Builder;

import java.util.List;

@Builder
public record EventHallResponse(Long id, String name, String address, List<EventHallSeatResponse> eventHallSeatResponses) {
    static public EventHallResponse of(EventHall eventHall){
        List<EventHallSeatResponse> eventHallSeatResponses = eventHall.getEventHallSeats().stream()
                .map(EventHallSeatResponse::of)
                .toList();

        return EventHallResponse.builder()
                .id(eventHall.getId())
                .name(eventHall.getName())
                .address(eventHall.getAddress())
                .eventHallSeatResponses(eventHallSeatResponses)
                .build();
    }
}
