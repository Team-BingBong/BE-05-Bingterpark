package com.pgms.apievent.eventtime.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.pgms.apievent.EventTestConfig;
import com.pgms.apievent.eventtime.dto.request.EventTimeCreateRequest;
import com.pgms.apievent.eventtime.dto.request.EventTimeUpdateRequest;
import com.pgms.apievent.eventtime.dto.response.EventTimeResponse;
import com.pgms.apievent.exception.EventException;
import com.pgms.apievent.factory.event.EventFactory;
import com.pgms.apievent.factory.eventhall.EventHallFactory;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

@SpringBootTest
@ContextConfiguration(classes = EventTestConfig.class)
class EventTimeServiceTest {

	private static final int REQUEST_NUMBER = 5;

	@Autowired
	private EventTimeService eventTimeService;

	@Autowired
	private EventTimeRepository eventTimeRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventHallRepository eventHallRepository;

	@BeforeEach
	void setUp() {
		eventRepository.deleteAll();
	}

	@AfterEach
	void tearDown() {
		eventTimeRepository.deleteAll();
	}

	@Test
	void 공연_회차_생성_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));
		EventTimeCreateRequest request = new EventTimeCreateRequest(1, event.getStartedAt(), event.getEndedAt());

		// When
		EventTimeResponse response = eventTimeService.createEventTime(event.getId(), request);

		// Then
		assertThat(response.round()).isEqualTo(1);
		assertThat(response.eventId()).isEqualTo(event.getId());
	}

	@Test
	void 공연_회차_중복_생성_실패_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		EventTimeCreateRequest request = new EventTimeCreateRequest(1, event.getStartedAt(), event.getEndedAt());
		EventTime eventTime = request.toEntity(event);
		eventTimeRepository.save(eventTime);

		// When & Then
		assertThatThrownBy(() -> eventTimeService.createEventTime(event.getId(), request))
			.isInstanceOf(EventException.class);
	}

	@Test
	void 공연_회차_단건_조회_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		EventTime eventTime = eventTimeRepository.save(
			new EventTime(1, event.getStartedAt(), event.getEndedAt(), event));

		// When
		EventTimeResponse response = eventTimeService.getEventTimeById(eventTime.getId());

		// Thgen
		assertThat(response.eventId()).isEqualTo(event.getId());
		assertThat(response.round()).isEqualTo(eventTime.getRound());
	}

	@Test
	void 공연_아이디로_회차_전체_조회_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		IntStream.range(0, REQUEST_NUMBER)
			.forEach(i -> eventTimeRepository.save(new EventTime(
				i + 1,
				event.getStartedAt(),
				event.getEndedAt(),
				event)));

		// When
		List<EventTimeResponse> responses = eventTimeService.getEventTimesByEventId(event.getId());

		// Then
		assertThat(responses).hasSize(REQUEST_NUMBER);
	}

	@Test
	void 공연_회차_수정_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		EventTime eventTime = eventTimeRepository.save(new EventTime(
			1,
			event.getStartedAt(),
			event.getEndedAt(),
			event));

		EventTimeUpdateRequest request = new EventTimeUpdateRequest(
			LocalDateTime.of(2023, 12, 31, 15, 0),
			LocalDateTime.of(2024, 1, 25, 18, 0));

		// When
		EventTimeResponse response = eventTimeService.updateEventTime(eventTime.getId(), request);

		// Then
		assertThat(response.startedAt()).isEqualTo(request.startedAt());
		assertThat(response.endedAt()).isEqualTo(request.endedAt());
	}

	@Test
	void 공연_회차_삭제_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		EventTime eventTime = eventTimeRepository.save(
			new EventTime(
				1,
				event.getStartedAt(),
				event.getEndedAt(),
				event)
		);

		// When
		eventTimeService.deleteEventTimeById(eventTime.getId());

		// Then
		assertThat(eventTimeRepository.findAll()).hasSize(0);
	}
}
