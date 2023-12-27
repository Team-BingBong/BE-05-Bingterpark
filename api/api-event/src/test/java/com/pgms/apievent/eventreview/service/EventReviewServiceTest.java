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
import com.pgms.apievent.eventreview.dto.request.EventReviewUpdateRequest;
import com.pgms.apievent.eventreview.dto.response.EventReviewResponse;
import com.pgms.apievent.factory.event.EventFactory;
import com.pgms.apievent.factory.eventhall.EventHallFactory;
import com.pgms.apievent.factory.eventreview.EventReviewFactory;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventReview;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventReviewRepository;

@SpringBootTest
class EventReviewServiceTest {

	private static final int REQUEST_NUMBER = 8;
	private static final double AVERAGE_REVIEW_SCORE = 3.6;

	@Autowired
	private EventReviewService eventReviewService;

	@Autowired
	private EventReviewRepository eventReviewRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventHallRepository eventHallRepository;

	private Event event;

	private EventReview eventReview;

	@BeforeEach
	void setUp() {
		EventHall eventHall = eventHallRepository.save(EventHallFactory.createEventHall());
		event = eventRepository.save(EventFactory.createEvent(eventHall));
		eventReview = eventReviewRepository.save(EventReviewFactory.createEventReview(event));
	}

	@AfterEach
	void tearDown() {
		eventReviewRepository.deleteAll();
	}

	@Test
	void 공연_리뷰_생성_테스트() {
		// Given
		EventReviewCreateRequest request = new EventReviewCreateRequest(5, "공연이 너무 재밌어요 !");

		// When
		EventReviewResponse response = eventReviewService.createEventReview(event.getId(), request);

		// Then
		assertThat(response.eventId()).isEqualTo(event.getId());
		assertThat(response.score()).isEqualTo(request.score());
		assertThat(response.content()).isEqualTo(request.content());
	}

	@Test
	void 공연_리뷰_평점_평균_계산_테스트() {
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

	@Test
	void 공연_리뷰_수정_테스트() {
		// Given
		EventReviewUpdateRequest request = new EventReviewUpdateRequest("공연 후기 수정입니다~!");

		// When
		EventReviewResponse response = eventReviewService.updateEventReview(eventReview.getId(), request);

		// Then
		assertThat(response.content()).isEqualTo(request.content());
	}

	@Test
	void 공연_리뷰_아이디로_단건_조회_테스트() {
		// Given
		Long eventReviewId = eventReview.getId();

		// When
		EventReviewResponse response = eventReviewService.getEventReviewById(eventReviewId);

		// Then
		assertThat(response.id()).isEqualTo(eventReview.getId());
		assertThat(response.score()).isEqualTo(eventReview.getScore());
		assertThat(response.content()).isEqualTo(eventReview.getContent());
	}

	@Test
	void 특정_공연_리뷰_전체_조회_테스트() {
		// Given
		Long eventId = event.getId();
		IntStream.range(0, REQUEST_NUMBER - 1)
			.forEach(i -> eventReviewRepository.save(new EventReview(i, "리뷰 내용 " + i, event)));

		// When
		List<EventReviewResponse> response = eventReviewService.getEventReviewsForEventByEventId(eventId);

		// Then
		assertThat(response).hasSize(REQUEST_NUMBER);
	}

	@Test
	void 공연_리뷰_아이디로_삭제_테스트() {
		// Given
		Long eventReviewId = eventReview.getId();

		// When
		eventReviewService.deleteEventReviewById(eventReviewId);

		// Then
		assertThat(eventReviewRepository.findAll()).hasSize(0);
	}
}
