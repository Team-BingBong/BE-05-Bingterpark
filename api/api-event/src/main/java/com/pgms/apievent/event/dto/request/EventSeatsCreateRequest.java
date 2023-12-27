package com.pgms.apievent.event.dto.request;

import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventSeatStatus;

import java.util.List;

public record EventSeatsCreateRequest(String name,
                                      EventSeatStatus status,
                                      EventSeatArea eventSeatArea,
                                      List<EventSeatsCreateRequest> requests) {
}
