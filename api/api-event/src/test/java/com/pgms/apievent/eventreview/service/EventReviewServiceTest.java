package com.pgms.apievent.eventreview.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pgms.apievent.eventreview.dto.request.EventReviewCreateRequest;
import com.pgms.apievent.factory.event.EventFactory;
import com.pgms.apievent.factory.eventhall.EventHallFactory;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventReviewRepository;

@SpringBootTest
class EventReviewServiceTest {

	private static final int REQUEST_NUMBER = 8;
	private static final double AVERAGE_REVIEW_SCORE = 3.5;

	@Autowired
	private EventReviewService eventReviewService;

	@Autowired
	private EventReviewRepository eventReviewRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventHallRepository eventHallRepository;

	private Event event;

	@BeforeEach
	void setUp() {
		EventHall eventHall = EventHallFactory.createEventHall();
		eventHallRepository.save(eventHall);
		event = eventRepository.save(EventFactory.createEvent(eventHall));
	}

	@AfterEach
	void tearDown() {
		eventReviewRepository.deleteAll();
	}

	@Test
	void 공연_리뷰_생성_테스트() {
		// Given
		List<EventReviewCreateRequest> requestList = IntStream.range(0, REQUEST_NUMBER)
			.mapToObj(i -> new EventReviewCreateRequest(i, "리뷰 내용 " + i))
			.toList();

		// When
		IntStream.range(0, REQUEST_NUMBER)
			.forEach(i -> eventReviewService.createEventReview(event.getId(), requestList.get(i)));
		Event savedEvent = eventRepository.findById(event.getId()).get();

		// Then
		assertThat(savedEvent.getAverageScore()).isEqualTo(AVERAGE_REVIEW_SCORE);
	}
}
