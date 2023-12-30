package com.pgms.apievent.event.service;

import com.pgms.apievent.event.dto.request.EventCreateRequest;
import com.pgms.apievent.event.dto.request.EventSeatAreaCreateRequest;
import com.pgms.apievent.event.dto.request.EventSeatAreaUpdateRequest;
import com.pgms.apievent.event.dto.request.EventUpdateRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.apievent.event.dto.response.EventSeatAreaResponse;
import com.pgms.apievent.exception.CustomException;
import com.pgms.apievent.exception.EventSeatAreaNotFoundException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventEdit;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;
import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.repository.EventSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pgms.apievent.exception.EventErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepository;
	private final EventHallRepository eventHallRepository;
	private final EventSeatAreaRepository eventSeatAreaRepository;
	private final EventSearchRepository eventSearchRepository;

	public EventResponse createEvent(EventCreateRequest request) {
		EventHall eventHall = getEventHall(request.eventHallId());
		validateDuplicateEvent(request.title());

		Event event = request.toEntity(eventHall);
		Event savedEvent = eventRepository.save(event);
		EventDocument from = EventDocument.from(savedEvent);
		eventSearchRepository.save(from);
		return EventResponse.of(event);
	}

	@Transactional(readOnly = true)
	public EventResponse getEventById(Long id) {
		Event event = getEvent(id);
		return EventResponse.of(event);
	}

	public EventResponse updateEvent(Long id, EventUpdateRequest request) {
		Event event = getEvent(id);
		EventEdit eventEdit = getEventEdit(request);
		event.updateEvent(eventEdit);
		return EventResponse.of(event);
	}

	public void deleteEventById(Long id) {
		Event event = getEvent(id);
		eventRepository.delete(event);
	}

	private void validateDuplicateEvent(String title) {
		if (Boolean.TRUE.equals((eventRepository.existsEventByTitle(title)))) {
			throw new CustomException(ALREADY_EXIST_EVENT);
		}
	}

	private EventEdit getEventEdit(EventUpdateRequest request) {
		EventHall eventHall = getEventHall(request.eventHallId());

		return EventEdit.builder()
			.title(request.title())
			.description(request.description())
			.runningTime(request.runningTime())
			.startDate(request.startDate())
			.endDate(request.endDate())
			.rating(request.rating())
			.genreType(request.genreType())
			.bookingStartedAt(request.bookingStartedAt())
			.bookingEndedAt(request.bookingEndedAt())
			.eventHall(eventHall)
			.build();
	}

	public List<EventSeatAreaResponse> createEventSeatArea(Long id, EventSeatAreaCreateRequest request) {
		Event event = getEvent(id);

		List<EventSeatArea> eventSeatAreas = request.requests().stream()
			.map(areaRequest -> new EventSeatArea(areaRequest.seatAreaType(), areaRequest.price(), event))
			.toList();

		List<EventSeatArea> savedEventSeatAreas = eventSeatAreaRepository.saveAll(eventSeatAreas);

		return savedEventSeatAreas.stream()
			.map(EventSeatAreaResponse::of)
			.toList();
	}

	public void deleteEventSeatArea(Long areaId) {
		EventSeatArea eventSeatArea = getEventSeatArea(areaId);
		eventSeatAreaRepository.delete(eventSeatArea);
	}

	public void updateEventSeatArea(Long areaId, EventSeatAreaUpdateRequest request) {
		EventSeatArea eventSeatArea = getEventSeatArea(areaId);
		eventSeatArea.updateEventSeatAreaPriceAndType(request.seatAreaType(), request.price());
	}

	@Transactional(readOnly = true)
	public List<EventSeatAreaResponse> getEventSeatAreas(Long id) {
		Event event = getEvent(id);
		List<EventSeatArea> eventSeatAreas = eventSeatAreaRepository.findEventSeatAreasByEvent(event);

		return eventSeatAreas.stream()
			.map(EventSeatAreaResponse::of)
			.toList();
	}

	private Event getEvent(Long eventId) {
		return eventRepository.findById(eventId)
			.orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
	}

	private EventHall getEventHall(Long eventHallId) {
		return eventHallRepository.findById(eventHallId)
			.orElseThrow(() -> new CustomException(EVENT_HALL_NOT_FOUND));
	}

	private EventSeatArea getEventSeatArea(Long areaId) {
		return eventSeatAreaRepository.findById(areaId)
			.orElseThrow(EventSeatAreaNotFoundException::new);
	}
}
