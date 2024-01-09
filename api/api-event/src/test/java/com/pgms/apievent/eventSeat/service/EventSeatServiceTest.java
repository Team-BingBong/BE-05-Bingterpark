package com.pgms.apievent.eventSeat.service;

import com.pgms.apievent.EventTestConfig;
import com.pgms.apievent.eventSeat.dto.request.EventSeatsCreateRequest;
import com.pgms.apievent.eventSeat.dto.response.EventSeatResponse;
import com.pgms.apievent.eventSeat.dto.response.LeftEventSeatResponse;
import com.pgms.apievent.factory.event.EventFactory;
import com.pgms.apievent.factory.eventhall.EventHallFactory;
import com.pgms.coredomain.domain.event.*;
import com.pgms.coredomain.domain.event.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Transactional
@SpringBootTest
@ContextConfiguration(classes = EventTestConfig.class)
class EventSeatServiceTest {

	@Autowired
	private EventSeatService eventSeatService;

	@Autowired
	private EventSeatRepository eventSeatRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventTimeRepository eventTimeRepository;

	@Autowired
	private EventHallRepository eventHallRepository;

	@Autowired
	private EventSeatAreaRepository eventSeatAreaRepository;

	@BeforeEach
	void setUp() {
		eventSeatRepository.deleteAll();
	}

	@Test
	void 공연_좌석_생성_성공() {
		// given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		List<EventTime> eventTimes = IntStream.range(1, 5)
			.mapToObj(i ->
				new EventTime(i, LocalDateTime.now(), LocalDateTime.now(), event))
			.toList();
		eventTimeRepository.saveAll(eventTimes);

		EventSeatArea eventSeatArea = new EventSeatArea(SeatAreaType.R, 1000, event);
		eventSeatAreaRepository.save(eventSeatArea);

		List<EventSeatsCreateRequest> eventSeatsCreateRequests = IntStream.range(0, 20)
			.mapToObj(i ->
				new EventSeatsCreateRequest("", EventSeatStatus.AVAILABLE, eventSeatArea.getId()))
			.toList();

		// when
		eventSeatService.createEventSeats(event.getId(), eventSeatsCreateRequests);

		// then
		assertThat(eventSeatRepository.count(), is(80L));
	}

	@Test
	void 좌석_생성_수정_후_여석_갯수_조회_성공() {
		// given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		// 좌석 생성
		EventTime eventTime = new EventTime(1, LocalDateTime.now(), LocalDateTime.now(), event);
		EventTime savedEventTime = eventTimeRepository.save(eventTime);

		EventSeatArea eventSeatArea = new EventSeatArea(SeatAreaType.R, 1000, event);
		eventSeatAreaRepository.save(eventSeatArea);

		List<EventSeatsCreateRequest> eventSeatsCreateRequests = IntStream.range(0, 20)
			.mapToObj(i ->
				new EventSeatsCreateRequest("", EventSeatStatus.AVAILABLE, eventSeatArea.getId()))
			.toList();
		eventSeatService.createEventSeats(event.getId(), eventSeatsCreateRequests);

		// 좌석 조회
		List<Long> seatIds = eventSeatService.getEventSeatsByEventTime(savedEventTime.getId())
			.stream()
			.map(EventSeatResponse::id)
			.limit(10)
			.toList();

		// 좌석 availability 수정
		eventSeatService.updateEventSeatsStatus(seatIds, EventSeatStatus.BOOKED);

		// when
		List<LeftEventSeatResponse> responses = eventSeatService.getLeftEventSeatNumberByEventTime(eventTime.getId());

		// then
		assertThat(responses.get(0).leftSeatNumber(), is(10L));
	}
}
