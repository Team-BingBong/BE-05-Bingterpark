package com.pgms.apievent.eventSeat.service;

import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.eventSeat.dto.response.EventSeatResponse;
import com.pgms.apievent.eventSeat.dto.response.LeftEventSeatResponse;
import com.pgms.apievent.eventSeat.repository.EventSeatCustomRepository;
import com.pgms.apievent.exception.CustomException;
import com.pgms.apievent.exception.EventSeatAreaNotFoundException;
import com.pgms.coredomain.domain.event.*;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pgms.apievent.exception.EventErrorCode.EVENT_NOT_FOUND;
import static com.pgms.apievent.exception.EventErrorCode.EVENT_TIME_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventSeatService {

    private final EventRepository eventRepository;
    private final EventTimeRepository eventTimeRepository;
    private final EventSeatRepository eventSeatRepository;
    private final EventSeatAreaRepository eventSeatAreaRepository;
    private final EventSeatCustomRepository eventSeatCustomRepository;

    public void createEventSeats(Long id, EventSeatsCreateRequest eventSeatsCreateRequest) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
        List<EventTime> eventTimes = eventTimeRepository.findEventTimesByEvent(event);

        List<List<EventSeat>> eventSeatsByEventTimes = eventTimes.stream()
                .map(eventTime ->
                        eventSeatsCreateRequest
                                .requests()
                                .stream()
                                .map(request -> EventSeat.builder()
                                        .eventTime(eventTime)
                                        .status(request.status())
                                        .eventSeatArea(request.eventSeatArea())
                                        .name(request.name())
                                        .build())
                                .toList())
                .toList();

        eventSeatsByEventTimes
                .forEach(eventSeatRepository::saveAll);
    }

    public void updateEventSeatsSeatArea(List<Long> ids, Long seatAreaId) {
        EventSeatArea eventSeatArea = eventSeatAreaRepository.findById(seatAreaId)
                .orElseThrow(EventSeatAreaNotFoundException::new);

        eventSeatCustomRepository.updateEventSeatsSeatArea(ids.toArray(new Long[0]), eventSeatArea);
    }

    public void updateEventSeatsStatus(List<Long> ids, EventSeatStatus eventSeatStatus) {
        eventSeatCustomRepository.updateEventSeatsStatus(ids.toArray(new Long[0]), eventSeatStatus);
    }

    public void deleteEventSeats(List<Long> ids) {
        eventSeatCustomRepository.deleteEventSeats(ids.toArray(new Long[0]));
    }

    @Transactional(readOnly = true)
    public List<EventSeatResponse> getEventSeatsByEventTime(Long id) {
        List<EventSeat> eventSeats = eventSeatRepository.findAllWithAreaByTimeId(id);

        List<EventSeatResponse> eventSeatResponses = eventSeats.stream()
                .map(EventSeatResponse::of)
                .toList();

        return eventSeatResponses;
    }

    @Transactional(readOnly = true)
    public List<LeftEventSeatResponse> getLeftEventSeatNumberByEventTime(Long id) {
        EventTime eventTime = eventTimeRepository.findById(id)
                .orElseThrow(() -> new CustomException(EVENT_TIME_NOT_FOUND));

        return eventSeatCustomRepository.getLeftEventSeatNumberByEventTime(eventTime)
                .stream()
                .map(leftEventSeatNumDto ->
                        new LeftEventSeatResponse(leftEventSeatNumDto.eventSeatArea().getSeatAreaType(), leftEventSeatNumDto.leftSeatNumber()))
                .toList();
    }
}
