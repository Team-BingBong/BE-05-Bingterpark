package com.pgms.apievent.eventtime.service;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.eventtime.dto.request.EventTimeCreateRequest;
import com.pgms.apievent.eventtime.dto.request.EventTimeUpdateRequest;
import com.pgms.apievent.eventtime.dto.response.EventTimeResponse;
import com.pgms.apievent.exception.EventException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class EventTimeService {

	private final EventTimeRepository eventTimeRepository;
	private final EventRepository eventRepository;

	public EventTimeResponse createEventTime(Long eventId, EventTimeCreateRequest request) {
		validateEventTimeRoundAlreadyExist(eventId, request.round());
		Event event = eventRepository.findById(eventId)
			.orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
		EventTime eventTime = eventTimeRepository.save(request.toEntity(event));
		return EventTimeResponse.of(eventTime);
	}

	@Transactional(readOnly = true)
	public EventTimeResponse getEventTimeById(Long eventTimeId) {
		EventTime eventTime = eventTimeRepository.findById(eventTimeId)
			.orElseThrow(() -> new EventException(EVENT_TIME_NOT_FOUND));
		return EventTimeResponse.of(eventTime);
	}

	@Transactional(readOnly = true)
	public List<EventTimeResponse> getEventTimesByEventId(Long eventId) {
		List<EventTime> eventTimes = eventTimeRepository.findEventTimesByEventId(eventId);
		return eventTimes.stream().map(EventTimeResponse::of).toList();
	}

	public EventTimeResponse updateEventTime(Long eventTimeId, EventTimeUpdateRequest request) {
		EventTime eventTime = eventTimeRepository.findById(eventTimeId)
			.orElseThrow(() -> new EventException(EVENT_TIME_NOT_FOUND));

		eventTime.updateEventTime(request.startTime(), request.endTime());
		return EventTimeResponse.of(eventTime);
	}

	public void deleteEventTimeById(Long eventTimeId) {
		EventTime eventTime = eventTimeRepository.findById(eventTimeId)
			.orElseThrow(() -> new EventException(EVENT_TIME_NOT_FOUND));
		eventTimeRepository.delete(eventTime);
	}

	private void validateEventTimeRoundAlreadyExist(Long eventId, int round) {
		if (eventTimeRepository.existsEventTimeForEventByRound(eventId, round)) {
			throw new EventException(ALREADY_EXIST_EVENT_TIME);
		}
	}
}
