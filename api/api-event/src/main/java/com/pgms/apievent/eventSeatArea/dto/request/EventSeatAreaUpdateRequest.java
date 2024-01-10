package com.pgms.apievent.eventSeatArea.dto.request;

import com.pgms.coredomain.domain.event.SeatAreaType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record EventSeatAreaUpdateRequest(
    SeatAreaType seatAreaType,
    @Min(0) @Max(1000000000)
    Integer price) {
}
