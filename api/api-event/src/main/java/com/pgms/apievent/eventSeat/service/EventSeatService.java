package com.pgms.apievent.eventSeat.service;

import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.eventSeat.dto.response.EventSeatResponse;
import com.pgms.apievent.exception.CustomException;
import com.pgms.apievent.exception.EventSeatAreaNotFoundException;
import com.pgms.coredomain.domain.event.*;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pgms.apievent.exception.EventErrorCode.EVENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EventSeatService {

    private final EventRepository eventRepository;
    private final EventTimeRepository eventTimeRepository;
    private final EventSeatRepository eventSeatRepository;
    private final EventSeatAreaRepository eventSeatAreaRepository;

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

    // TODO bulk update 도 고려
    public void updateEventSeatsSeatArea(List<Long> ids, Long seatAreaId) {
        EventSeatArea eventSeatArea = eventSeatAreaRepository.findById(seatAreaId)
                .orElseThrow(EventSeatAreaNotFoundException::new);

        ids.forEach(id -> {
            EventSeat eventSeat = eventSeatRepository.findById(id)
                    .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
            eventSeat.updateEventSeatArea(eventSeatArea);
        });
    }

    public void updateEventSeatsStatus(List<Long> ids, EventSeatStatus eventSeatStatus) {
        ids.forEach(id -> {
            EventSeat eventSeat = eventSeatRepository.findById(id)
                    .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
            eventSeat.updateStatus(eventSeatStatus);
        });
    }

    public void deleteEventSeats(List<Long> ids) {
        List<EventSeat> eventSeats = ids.stream()
                .map(id ->
                        eventSeatRepository.findById(id)
                                .orElseThrow(() -> new CustomException(EVENT_NOT_FOUND)))
                .toList();

        eventSeatRepository.deleteAllInBatch(eventSeats);
    }

    public List<EventSeatResponse> getEventSeatsByEventTime(Long id) {
        List<EventSeat> eventSeats = eventSeatRepository.findAllWithAreaByEventTimeId(id);

        List<EventSeatResponse> eventSeatResponses = eventSeats.stream()
                .map(EventSeatResponse::of)
                .toList();

        return eventSeatResponses;
    }
}
