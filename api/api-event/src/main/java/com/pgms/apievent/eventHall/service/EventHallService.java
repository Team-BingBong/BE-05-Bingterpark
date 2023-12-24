package com.pgms.apievent.eventHall.service;

import com.pgms.apievent.eventHall.dto.request.EventHallCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallSeatCreateRequest;
import com.pgms.apievent.eventHall.dto.response.EventHallResponse;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventHallSeat;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EventHallService {

    private final EventHallRepository eventHallRepository;

    public EventHallResponse createEventHall(EventHallCreateRequest eventHallCreateRequest) {
        List<EventHallSeatCreateRequest> eventHallSeatCreateRequests = eventHallCreateRequest.eventHallSeatCreateRequests();

        List<EventHallSeat> eventHallSeats = eventHallSeatCreateRequests.stream()
                .map(this::createEventHallSeat)
                .collect(Collectors.toList());

        EventHall eventHall = EventHall.builder()
                .name(eventHallCreateRequest.name())
                .address(eventHallCreateRequest.address())
                .eventHallSeats(eventHallSeats)
                .build();

        return EventHallResponse.of(eventHallRepository.save(eventHall));
    }

    public EventHallSeat createEventHallSeat(EventHallSeatCreateRequest eventHallSeatCreateRequest) {
        return new EventHallSeat(eventHallSeatCreateRequest.name());
    }
}
