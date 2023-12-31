package com.pgms.apievent.eventSeat.service;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.eventSeat.dto.response.EventSeatResponse;
import com.pgms.apievent.eventSeat.dto.response.LeftEventSeatResponse;
import com.pgms.apievent.eventSeat.repository.EventSeatCustomRepository;
import com.pgms.apievent.exception.EventException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventSeatService {

	private final EventRepository eventRepository;
	private final EventTimeRepository eventTimeRepository;
	private final EventSeatRepository eventSeatRepository;
	private final EventSeatAreaRepository eventSeatAreaRepository;
	private final EventSeatCustomRepository eventSeatCustomRepository;

	public void createEventSeats(Long id, List<EventSeatsCreateRequest> eventSeatsCreateRequests) {
		Event event = eventRepository.findById(id)
			.orElseThrow(() -> new EventException(EVENT_NOT_FOUND));
		List<EventTime> eventTimes = eventTimeRepository.findEventTimesByEvent(event);

		List<List<EventSeat>> eventSeatsByEventTimes = eventTimes.stream()
			.map(eventTime ->
				eventSeatsCreateRequests
					.stream()
					.map(request -> {
						EventSeatArea eventSeatArea = eventSeatAreaRepository
							.findById(request.eventSeatAreaId())
							.orElseThrow(() -> new EventException(EVENT_SEAT_AREA_NOT_FOUND));

						return EventSeat.builder()
							.eventTime(eventTime)
							.status(EventSeatStatus.valueOf(request.status()))
							.eventSeatArea(eventSeatArea)
							.name(request.name())
							.build();
					})
					.toList())
			.toList();

		eventSeatsByEventTimes
			.forEach(eventSeatRepository::saveAll);
	}

	public void updateEventSeatsSeatArea(List<Long> ids, Long seatAreaId) {
		EventSeatArea eventSeatArea = eventSeatAreaRepository.findById(seatAreaId)
			.orElseThrow(() -> new EventException(EVENT_SEAT_AREA_NOT_FOUND));

		eventSeatCustomRepository.updateEventSeatsSeatArea(ids.toArray(new Long[0]), eventSeatArea);
	}

	public void updateEventSeatsStatus(List<Long> ids, EventSeatStatus eventSeatStatus) {
		eventSeatCustomRepository.updateEventSeatsStatus(ids.toArray(new Long[0]), eventSeatStatus);
	}

	public void deleteEventSeats(List<Long> ids) {
		eventSeatCustomRepository.deleteEventSeats(ids.toArray(new Long[0]));
	}

	@Transactional(readOnly = true)
	public List<EventSeatResponse> getEventSeatsByEventTime(Long id) {
		List<EventSeat> eventSeats = eventSeatRepository.findAllWithAreaByTimeId(id);

		return eventSeats.stream()
			.map(EventSeatResponse::of)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<LeftEventSeatResponse> getLeftEventSeatNumberByEventTime(Long id) {
		EventTime eventTime = eventTimeRepository.findById(id)
			.orElseThrow(() -> new EventException(EVENT_TIME_NOT_FOUND));

		return eventSeatCustomRepository.getLeftEventSeatNumberByEventTime(eventTime)
			.stream()
			.map(leftEventSeatNumDto ->
				new LeftEventSeatResponse(leftEventSeatNumDto.getEventSeatArea().getSeatAreaType(),
					leftEventSeatNumDto.getLeftSeatNumber()))
			.toList();
	}
}
