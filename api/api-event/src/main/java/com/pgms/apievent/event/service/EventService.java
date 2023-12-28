package com.pgms.apievent.event.service;

import com.pgms.apievent.event.dto.request.*;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.apievent.event.dto.response.EventSeatAreaResponse;
import com.pgms.apievent.event.dto.response.EventSeatResponse;
import com.pgms.apievent.exception.CustomException;
import com.pgms.apievent.exception.EventSeatAreaNotFoundException;
import com.pgms.coredomain.domain.event.*;
import com.pgms.coredomain.domain.event.repository.*;
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
	private final EventTimeRepository eventTimeRepository;
	private final EventSeatRepository eventSeatRepository;
	private final EventSeatAreaRepository eventSeatAreaRepository;

	public EventResponse createEvent(EventCreateRequest request) {
		validateDuplicateEvent(request.title());

		EventHall eventHall = eventHallRepository.findById(request.eventHallId())
				.orElseThrow(() -> new CustomException(EVENT_HALL_NOT_FOUND));

		Event event = request.toEntity(eventHall);
		eventRepository.save(event);
		return EventResponse.of(event);
	}

	@Transactional(readOnly = true)
	public EventResponse getEventById(Long id) {
		Event event = eventRepository.findById(id)
			.orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
		return EventResponse.of(event);
	}

	public EventResponse updateEvent(Long id, EventUpdateRequest request) {
		Event event = eventRepository.findById(id)
			.orElseThrow();
		EventEdit eventEdit = getEventEdit(request);
		event.updateEvent(eventEdit);
		return EventResponse.of(event);
	}

	public void deleteEventById(Long id) {
		Event event = eventRepository.findById(id)
			.orElseThrow();
		eventRepository.delete(event);
	}

	private void validateDuplicateEvent(String title) {
		if (Boolean.TRUE.equals((eventRepository.existsEventByTitle(title)))) {
			throw new CustomException(ALREADY_EXIST_EVENT);
		}
	}

	private EventEdit getEventEdit(EventUpdateRequest request) {
		EventHall eventHall = eventHallRepository.findById(request.eventHallId())
			.orElseThrow(() -> new CustomException(EVENT_HALL_NOT_FOUND));

		return EventEdit.builder()
			.title(request.title())
			.description(request.description())
			.runningTime(request.runningTime())
			.startDate(request.startDate())
			.endDate(request.endDate())
			.rating(request.rating())
			.genreType(request.genreType())
			.thumbnail(request.thumbnail())
			.eventHall(eventHall)
			.build();
	}

    public List<EventSeatAreaResponse> createEventSeatArea(Long id, EventSeatAreaCreateRequest request) {
		Event event = eventRepository.findById(id)
				.orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));

		List<EventSeatArea> eventSeatAreas = request.requests().stream()
				.map(areaRequest -> new EventSeatArea(areaRequest.seatAreaType(), areaRequest.price(), event))
				.toList();

		List<EventSeatArea> savedEventSeatAreas = eventSeatAreaRepository.saveAll(eventSeatAreas);

		return savedEventSeatAreas.stream()
				.map(EventSeatAreaResponse::of)
				.toList();
	}

	public void deleteEventSeatArea(Long areaId) {
		EventSeatArea eventSeatArea = eventSeatAreaRepository.findById(areaId)
				.orElseThrow(EventSeatAreaNotFoundException::new);

		eventSeatAreaRepository.delete(eventSeatArea);
	}

	public void updateEventSeatArea(Long areaId, EventSeatAreaUpdateRequest request) {
		EventSeatArea eventSeatArea = eventSeatAreaRepository.findById(areaId)
				.orElseThrow(EventSeatAreaNotFoundException::new);

		eventSeatArea.updateEventSeatAreaPriceAndType(request.seatAreaType(), request.price());
	}

	public List<EventSeatAreaResponse> getEventSeatAreas(Long id) {
		Event event = eventRepository.findById(id)
				.orElseThrow(() -> new CustomException(EVENT_NOT_FOUND));
		List<EventSeatArea> eventSeatAreas = eventSeatAreaRepository.findEventSeatAreasByEvent(event);

		return eventSeatAreas.stream()
				.map(EventSeatAreaResponse::of)
				.toList();
	}

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
		List<EventSeat> eventSeats = eventSeatRepository.findAllWithAreaByTimeId(id);

		List<EventSeatResponse> eventSeatResponses = eventSeats.stream()
				.map(EventSeatResponse::of)
				.toList();

		return eventSeatResponses;
	}
}
