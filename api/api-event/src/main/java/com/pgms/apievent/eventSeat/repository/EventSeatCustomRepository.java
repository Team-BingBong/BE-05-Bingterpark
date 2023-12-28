package com.pgms.apievent.eventSeat.repository;

import com.pgms.apievent.eventSeat.dto.LeftEventSeatNumDto;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;

import java.util.List;

public interface EventSeatCustomRepository {

    void updateEventSeatsSeatArea(Long[] ids, EventSeatArea eventSeatArea);

    void updateEventSeatsStatus(Long[] ids, EventSeatStatus eventSeatStatus);

    List<LeftEventSeatNumDto> getLeftEventSeatNumberByEventTime(EventTime eventTime);
}
