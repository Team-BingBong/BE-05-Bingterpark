package com.pgms.apievent.eventSeat.dto;

import com.pgms.coredomain.domain.event.EventSeatArea;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LeftEventSeatNumDto {
    private EventSeatArea eventSeatArea;
    private Long leftSeatNumber;
}
