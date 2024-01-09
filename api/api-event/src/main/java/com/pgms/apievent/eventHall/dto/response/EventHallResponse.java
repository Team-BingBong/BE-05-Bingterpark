package com.pgms.apievent.eventHall.dto.response;

import com.pgms.coredomain.domain.event.EventHall;
import lombok.Builder;

import java.util.List;

@Builder
public record EventHallResponse(Long id, String name, String address, List<EventHallSeatResponse> eventHallSeatResponses) {
    static public EventHallResponse of(EventHall eventHall, List<EventHallSeatResponse> eventHallSeatResponses){
        return EventHallResponse.builder()
                .id(eventHall.getId())
                .name(eventHall.getName())
                .address(eventHall.getAddress())
                .eventHallSeatResponses(eventHallSeatResponses)
                .build();
    }
}
