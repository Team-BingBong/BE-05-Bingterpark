package com.pgms.apievent.event.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pgms.apievent.event.dto.request.EventCreateRequest;
import com.pgms.apievent.event.dto.request.EventUpdateRequest;
import com.pgms.apievent.event.dto.response.EventResponse;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.GenreType;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;

@SpringBootTest
class EventServiceTest {

	@Autowired
	private EventService eventService;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventHallRepository eventHallRepository;

	private EventHall eventHall;

	private Event event;

	@BeforeEach
	void setUp() {
		eventHall = new EventHall("테스트 공연장", "테스트 주소", new ArrayList<>());
		eventHallRepository.save(eventHall);

		EventCreateRequest request = new EventCreateRequest(
			"공연 1",
			"공연1 입니다.",
			60,
			LocalDateTime.of(2023, 1, 1, 0, 0),  // 예시로 날짜와 시간을 지정
			LocalDateTime.of(2023, 1, 7, 0, 0),
			"12세 이상",
			GenreType.MUSICAL,
			"thumbnail.jpg",
			1L
		);
		event = eventRepository.save(request.toEntity(eventHall));
	}

	@AfterEach
	void tearDown() {
		eventRepository.deleteAll();
	}

	@Test
	void 공연_아이디로_조회_테스트() {
		// Given
		Long eventId = event.getId();

		// When
		EventResponse response = eventService.getEventById(eventId);

		// Then
		assertThat(response.id()).isEqualTo(event.getId());
		assertThat(response.title()).isEqualTo(event.getTitle());
	}

	@Test
	void 공연_수정_테스트() {
		// Given
		Long eventId = event.getId();
		EventUpdateRequest request = new EventUpdateRequest(
			"공연 1 수정",
			"공연 1 내용 수정입니다.",
			100,
			LocalDateTime.of(2023, 2, 1, 0, 0),
			LocalDateTime.of(2023, 2, 1, 0, 0),
			"15세 이상",
			GenreType.MUSICAL,
			"thumbnail.jpg",
			eventHall.getId());

		// When
		EventResponse response = eventService.updateEvent(eventId, request);

		// Then
		assertThat(response.title()).isEqualTo(request.title());
		assertThat(response.rating()).isEqualTo(request.rating());
	}

	@Test
	void 공연_삭제_테스트() {
		// Given
		Long eventId = event.getId();

		// When
		eventService.deleteEventById(eventId);

		// Then
		List<Event> events = eventRepository.findAll();
		assertThat(events).hasSize(0);
	}
}
