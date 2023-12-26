package com.pgms.apievent.eventtime.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pgms.apievent.eventtime.dto.request.EventTimeCreateRequest;
import com.pgms.apievent.eventtime.dto.request.EventTimeUpdateRequest;
import com.pgms.apievent.eventtime.dto.response.EventTimeResponse;
import com.pgms.apievent.exception.CustomException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.GenreType;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

@SpringBootTest
class EventTimeServiceTest {

	@Autowired
	private EventTimeService eventTimeService;

	@Autowired
	private EventTimeRepository eventTimeRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventHallRepository eventHallRepository;

	private Event event;

	@BeforeEach
	void setUp() {
		EventHall eventHall = eventHallRepository.save(new EventHall("테스트 공연장", "테스트 주소", new ArrayList<>()));
		event = eventRepository.save(new Event(
			"공연 1 수정",
			"공연 1 내용 수정입니다.",
			100,
			LocalDateTime.of(2023, 2, 1, 0, 0),
			LocalDateTime.of(2023, 2, 1, 0, 0),
			"15세 이상",
			GenreType.MUSICAL,
			"thumbnail.jpg",
			eventHall));
	}

	@AfterEach
	void tearDown() {
		eventTimeRepository.deleteAll();
	}

	@Test
	void 공연_회차_생성_테스트() {
		// Given
		EventTimeCreateRequest request = new EventTimeCreateRequest(1, event.getStartDate(), event.getEndDate());

		// When
		EventTimeResponse response = eventTimeService.createEventTime(event.getId(), request);

		// Then
		assertThat(response.round()).isEqualTo(1);
		assertThat(response.eventId()).isEqualTo(event.getId());
	}

	@Test
	void 공연_회차_중복_생성_실패_테스트() {
		// Given
		EventTimeCreateRequest request = new EventTimeCreateRequest(1, event.getStartDate(), event.getEndDate());
		EventTime eventTime = request.toEntity(event);
		eventTimeRepository.save(eventTime);

		// When & Then
		assertThatThrownBy(() -> eventTimeService.createEventTime(event.getId(), request))
			.isInstanceOf(CustomException.class);
	}

	@Test
	void 공연_단건_조회_테스트() {
		// Given
		EventTime eventTime = eventTimeRepository.save(
			new EventTime(1, event.getStartDate(), event.getEndDate(), event));

		// When
		EventTimeResponse response = eventTimeService.getEventTimeById(eventTime.getId());

		// Thgen
		assertThat(response.eventId()).isEqualTo(event.getId());
		assertThat(response.round()).isEqualTo(eventTime.getRound());
	}

	@Test
	void 공연_수정_테스트() {
		// Given
		EventTime eventTime = eventTimeRepository.save(
			new EventTime(1, event.getStartDate(), event.getEndDate(), event));
		EventTimeUpdateRequest request = new EventTimeUpdateRequest(
			LocalDateTime.of(2023, 12, 31, 15, 0),
			LocalDateTime.of(2024, 1, 25, 18, 0));

		// When
		EventTimeResponse response = eventTimeService.updateEventTime(eventTime.getId(), request);

		// Then
		assertThat(response.startTime()).isEqualTo(request.startTime());
		assertThat(response.endTime()).isEqualTo(request.endTime());
	}

	@Test
	void 공연_삭제_테스트() {
		// Given
		EventTime eventTime = eventTimeRepository.save(
			new EventTime(1, event.getStartDate(), event.getEndDate(), event));

		// When
		eventTimeService.deleteEventTimeById(eventTime.getId());

		// Then
		assertThat(eventTimeRepository.findAll()).hasSize(0);
	}
}
