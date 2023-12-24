package com.pgms.apievent.event.service;

import static com.pgms.apievent.exception.EventErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.event.dto.request.EventCreateRequest;
import com.pgms.apievent.event.dto.request.EventUpdateRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.apievent.exception.CustomException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventEdit;
import com.pgms.coredomain.domain.event.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

	private final EventRepository eventRepository;
	// private final EventHallRepository eventHallRepository;

	public EventResponse createEvent(EventCreateRequest request) {
		validateDuplicateEvent(request.title());
		/**
		 * TODO
		 *  1) 공연장 정보를 통해 공연장 가져오기
		 *  2) toEntity 메소드에 eventHall 넘겨주기
		 */
		Event event = request.toEntity(null);
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
		/**
		 * TODO
		 *  eventHall 찾아서 eventHall 부분에 넣어주기
		 */
		return EventEdit.builder()
			.title(request.title())
			.description(request.description())
			.runningTime(request.runningTime())
			.startDate(request.startDate())
			.endDate(request.endDate())
			.rating(request.rating())
			.genreType(request.genreType())
			.thumbnail(request.thumbnail())
			.eventHall(null).build();
	}
}
