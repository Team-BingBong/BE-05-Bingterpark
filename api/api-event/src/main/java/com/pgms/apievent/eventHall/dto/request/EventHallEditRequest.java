package com.pgms.apievent.eventHall.dto.request;

import java.util.List;

public record EventHallEditRequest(String name, String address, List<EventHallSeatCreateRequest> eventHallSeatCreateRequests) {
}
