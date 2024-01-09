package com.pgms.apievent.eventSeatArea.service;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.eventSeatArea.dto.request.EventSeatAreaCreateRequest;
import com.pgms.apievent.eventSeatArea.dto.request.EventSeatAreaUpdateRequest;
import com.pgms.apievent.eventSeatArea.dto.response.EventSeatAreaResponse;
import com.pgms.apievent.exception.EventException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.SeatAreaType;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventSeatAreaService {

	private final EventRepository eventRepository;
	private final EventSeatAreaRepository eventSeatAreaRepository;

	public List<EventSeatAreaResponse> createEventSeatArea(Long id, EventSeatAreaCreateRequest request) {
		Event event = getEvent(id);

		List<EventSeatArea> eventSeatAreas = request.requests().stream()
			.map(areaRequest -> new EventSeatArea(
				SeatAreaType.valueOf(areaRequest.seatAreaType()),
				areaRequest.price(), event))
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
			.orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
	}

	private EventSeatArea getEventSeatArea(Long areaId) {
		return eventSeatAreaRepository.findById(areaId)
			.orElseThrow(() -> new EventException(EVENT_SEAT_AREA_NOT_FOUND));
	}
}
