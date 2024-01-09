package com.pgms.apievent.eventHall.service;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.eventHall.dto.request.EventHallCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallSeatCreateRequest;
import com.pgms.apievent.eventHall.dto.request.EventHallUpdateRequest;
import com.pgms.apievent.eventHall.dto.response.EventHallResponse;
import com.pgms.apievent.exception.EventException;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventHallEdit;
import com.pgms.coredomain.domain.event.EventHallSeat;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;

import lombok.RequiredArgsConstructor;

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

		return EventHallResponse.of(eventHallRepository.save(eventHall));
	}

	public void deleteEventHall(Long id) {
		EventHall eventHall = eventHallRepository.findById(id)
			.orElseThrow(RuntimeException::new);

		eventHallRepository.delete(eventHall);
	}

	public EventHallResponse updateEventHall(Long id, EventHallUpdateRequest eventHallUpdateRequest) {
		EventHall eventHall = eventHallRepository.findById(id)
			.orElseThrow(() -> new EventException(EVENT_HALL_NOT_FOUND));

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

		return EventHallResponse.of(eventHall);
	}

	@Transactional(readOnly = true)
	public EventHallResponse getEventHall(Long id) {
		EventHall eventHall = eventHallRepository.findById(id)
			.orElseThrow(() -> new EventException(EVENT_HALL_NOT_FOUND));

		return EventHallResponse.of(eventHall);
	}

	@Transactional(readOnly = true)
	public List<EventHallResponse> getEventHalls() {
		List<EventHall> eventHalls = eventHallRepository.findAll();

		return eventHalls.stream()
			.map(EventHallResponse::of)
			.toList();
	}

	private EventHallSeat createEventHallSeat(EventHallSeatCreateRequest eventHallSeatCreateRequest) {
		return new EventHallSeat(eventHallSeatCreateRequest.name());
	}
}
