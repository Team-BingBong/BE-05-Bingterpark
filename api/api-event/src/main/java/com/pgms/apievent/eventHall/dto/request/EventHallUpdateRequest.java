package com.pgms.apievent.eventHall.dto.request;

import java.util.List;

public record EventHallUpdateRequest(String name, String address, List<EventHallSeatCreateRequest> eventHallSeatCreateRequests) {
}
