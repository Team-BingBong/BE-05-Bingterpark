package com.pgms.apievent.event.service;

import static com.pgms.apievent.exception.EventErrorCode.*;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.common.dto.response.PageResponseDto;
import com.pgms.apievent.event.dto.request.EventCreateRequest;
import com.pgms.apievent.event.dto.request.EventPageRequest;
import com.pgms.apievent.event.dto.request.EventUpdateRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.apievent.event.repository.EventCustomRepository;
import com.pgms.apievent.exception.EventException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventEdit;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coreinfraes.buffer.DocumentBuffer;
import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.repository.EventSearchRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepository;
	private final EventHallRepository eventHallRepository;
	private final EventSearchRepository eventSearchRepository;
	private final EventCustomRepository eventCustomRepository;

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

	@Transactional(readOnly = true)
	public PageResponseDto getEventsPageByGenreSortedByRanking(EventPageRequest request) {
		Page<EventResponse> events = eventCustomRepository.getEventsPageByGenreSortedByRanking(request);
		return PageResponseDto.of(events);
	}

	@Transactional(readOnly = true)
	public PageResponseDto getEventsPageByGenreSortedByReview(EventPageRequest request) {
		Page<EventResponse> events = eventCustomRepository.getEventsPageByGenreSortedByReview(request);
		return PageResponseDto.of(events);
	}

	@Transactional(readOnly = true)
	public PageResponseDto getEventsPageByGenreSortedByBookingEndedAt(EventPageRequest request) {
		Page<EventResponse> events = eventCustomRepository.getEventsPageByGenreSortedByBookingEndedAt(request);
		return PageResponseDto.of(events);
	}

	public EventResponse updateEvent(Long id, EventUpdateRequest request) {
		Event event = getEvent(id);
		EventEdit eventEdit = getEventEdit(request);
		event.updateEvent(eventEdit);

		DocumentBuffer.add(EventDocument.from(event));
		return EventResponse.of(event);
	}

	public void deleteEventById(Long id) {
		Event event = getEvent(id);
		eventRepository.delete(event);
		eventSearchRepository.deleteById(id);
	}

	private void validateDuplicateEvent(String title) {
		if (Boolean.TRUE.equals((eventRepository.existsEventByTitle(title)))) {
			throw new EventException(ALREADY_EXIST_EVENT);
		}
	}

	private EventEdit getEventEdit(EventUpdateRequest request) {
		EventHall eventHall = getEventHall(request.eventHallId());

		return EventEdit.builder()
			.title(request.title())
			.description(request.description())
			.runningTime(request.runningTime())
			.startDate(request.startedAt())
			.endDate(request.endedAt())
			.viewRating(request.viewRating())
			.genreType(request.genreType())
			.bookingStartedAt(request.bookingStartedAt())
			.bookingEndedAt(request.bookingEndedAt())
			.eventHall(eventHall)
			.build();
	}

	private Event getEvent(Long eventId) {
		return eventRepository.findById(eventId)
			.orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
	}

	private EventHall getEventHall(Long eventHallId) {
		return eventHallRepository.findById(eventHallId)
			.orElseThrow(() -> new EventException(EVENT_HALL_NOT_FOUND));
	}
}
