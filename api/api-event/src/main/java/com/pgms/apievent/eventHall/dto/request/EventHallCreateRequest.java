package com.pgms.apievent.eventHall.dto.request;

import java.util.List;

public record EventHallCreateRequest(String name,
                                     String address,
                                     List<EventHallSeatCreateRequest> eventHallSeatCreateRequests) {
}
