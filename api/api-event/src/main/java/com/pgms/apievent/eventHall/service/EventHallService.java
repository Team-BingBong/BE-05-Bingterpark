package com.pgms.apievent.eventHall.service;

import com.pgms.apievent.eventHall.dto.request.EventHallCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallUpdateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallSeatCreateRequest;
import com.pgms.apievent.eventHall.dto.response.EventHallResponse;
import com.pgms.apievent.eventHall.dto.response.EventHallSeatResponse;
import com.pgms.apievent.exception.EventHallNotFoundException;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventHallEdit;
import com.pgms.coredomain.domain.event.EventHallSeat;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EventHallService {

    private final EventHallRepository eventHallRepository;

    public EventHallResponse createEventHall(EventHallCreateRequest eventHallCreateRequest) {
        List<EventHallSeatCreateRequest> eventHallSeatCreateRequests = eventHallCreateRequest.eventHallSeatCreateRequests();

        List<EventHallSeat> eventHallSeats = eventHallSeatCreateRequests.stream()
                .map(this::createEventHallSeat)
                .toList();

        EventHall eventHall = EventHall.builder()
                .name(eventHallCreateRequest.name())
                .address(eventHallCreateRequest.address())
                .eventHallSeats(eventHallSeats)
                .build();

        EventHall savedEventHall = eventHallRepository.save(eventHall);

        List<EventHallSeatResponse> eventHallSeatResponses = savedEventHall.getEventHallSeats()
                .stream()
                .map(EventHallSeatResponse::of)
                .toList();

        return EventHallResponse.of(savedEventHall, eventHallSeatResponses);
    }

    public void deleteEventHall(Long id) {
        EventHall eventHall = eventHallRepository.findById(id)
                .orElseThrow(RuntimeException::new);

        eventHallRepository.delete(eventHall);
    }

    public EventHallResponse updateEventHall(Long id, EventHallUpdateRequest eventHallUpdateRequest) {
        EventHall eventHall = eventHallRepository.findById(id).orElseThrow(EventHallNotFoundException::new);

        List<EventHallSeatCreateRequest> eventHallSeatCreateRequests = eventHallUpdateRequest.eventHallSeatCreateRequests();

        List<EventHallSeat> eventHallSeats = eventHallSeatCreateRequests.stream()
                .map(this::createEventHallSeat)
                .toList();

        EventHallEdit eventHallEdit = EventHallEdit.builder()
                .name(eventHallUpdateRequest.name())
                .address(eventHallUpdateRequest.address())
                .eventHallSeats(eventHallSeats)
                .build();

        eventHall.updateEventHall(eventHallEdit);

        List<EventHallSeatResponse> eventHallSeatResponses = eventHall.getEventHallSeats()
                .stream()
                .map(EventHallSeatResponse::of)
                .toList();

        return EventHallResponse.of(eventHall, eventHallSeatResponses);
    }

    @Transactional(readOnly = true)
    public EventHallResponse getEventHall(Long id) {
        EventHall eventHall = eventHallRepository.findById(id).orElseThrow(EventHallNotFoundException::new);

        // TODO 못 읽어옴
        List<EventHallSeatResponse> eventHallSeatResponses = eventHall.getEventHallSeats()
                .stream()
                .map(EventHallSeatResponse::of)
                .toList();

        return EventHallResponse.of(eventHall, eventHallSeatResponses);
    }

    @Transactional(readOnly = true)
    public List<EventHallResponse> getEventHalls() {
        List<EventHall> eventHalls = eventHallRepository.findAll();
        return eventHalls.stream()
                .map(eventHall -> EventHallResponse.of(eventHall, null))
                .toList();
    }

    private EventHallSeat createEventHallSeat(EventHallSeatCreateRequest eventHallSeatCreateRequest) {
        return new EventHallSeat(eventHallSeatCreateRequest.name());
    }
}
