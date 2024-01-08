package com.pgms.apievent.event.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.pgms.apievent.EventTestConfig;
import com.pgms.apievent.event.dto.request.EventUpdateRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.apievent.factory.event.EventFactory;
import com.pgms.apievent.factory.eventhall.EventHallFactory;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.GenreType;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;

@SpringBootTest
@ContextConfiguration(classes = EventTestConfig.class)
class EventServiceTest {

	@Autowired
	private EventService eventService;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventHallRepository eventHallRepository;

	@BeforeEach
	void setUp() {
		eventRepository.deleteAll();
	}

	@Test
	void 공연_아이디로_조회_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		// When
		EventResponse response = eventService.getEventById(event.getId());

		// Then
		assertThat(response.id()).isEqualTo(event.getId());
		assertThat(response.title()).isEqualTo(event.getTitle());
	}

	@Test
	void 공연_수정_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		EventUpdateRequest request = new EventUpdateRequest(
			"공연 1 수정",
			"공연 1 내용 수정입니다.",
			100,
			LocalDateTime.of(2023, 2, 1, 0, 0),
			LocalDateTime.of(2023, 2, 1, 0, 0),
			"15세 이상",
			GenreType.MUSICAL,
			LocalDateTime.of(2023, 2, 1, 0, 0),
			LocalDateTime.of(2023, 2, 1, 0, 0),
			eventHall.getId());

		// When
		EventResponse response = eventService.updateEvent(event.getId(), request);

		// Then
		assertThat(response.title()).isEqualTo(request.title());
		assertThat(response.rating()).isEqualTo(request.viewRating());
	}

	@Test
	void 공연_삭제_테스트() {
		// Given
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));

		// When
		eventService.deleteEventById(event.getId());

		// Then
		List<Event> events = eventRepository.findAll();
		assertThat(events).hasSize(0);
	}
}
