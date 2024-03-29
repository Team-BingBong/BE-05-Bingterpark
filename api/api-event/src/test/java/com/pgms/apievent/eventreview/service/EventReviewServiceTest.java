package com.pgms.apievent.eventreview.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.pgms.apievent.EventTestConfig;
import com.pgms.apievent.eventreview.dto.request.EventReviewCreateRequest;
import com.pgms.apievent.eventreview.dto.request.EventReviewUpdateRequest;
import com.pgms.apievent.eventreview.dto.response.EventReviewResponse;
import com.pgms.apievent.factory.event.EventFactory;
import com.pgms.apievent.factory.eventhall.EventHallFactory;
import com.pgms.apievent.factory.eventreview.EventReviewFactory;
import com.pgms.apievent.factory.member.MemberFactory;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventReview;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventReviewRepository;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.repository.MemberRepository;

@SpringBootTest
@ContextConfiguration(classes = EventTestConfig.class)
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

	@Autowired
	private MemberRepository memberRepository;

	@AfterEach
	void tearDown() {
		eventReviewRepository.deleteAll();
	}

	@Test
	void 공연_리뷰_생성_테스트() {
		// Given
		EventHall eventHall = eventHallRepository.save(EventHallFactory.createEventHall());
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));
		Member member = memberRepository.save(MemberFactory.createMember());

		EventReviewCreateRequest request = new EventReviewCreateRequest(5, "공연이 너무 재밌어요 !");

		// When
		EventReviewResponse response = eventReviewService.createEventReview(member.getId(), event.getId(), request);

		// Then
		assertThat(response.eventId()).isEqualTo(event.getId());
		assertThat(response.score()).isEqualTo(request.score());
		assertThat(response.content()).isEqualTo(request.content());
	}

	@Test
	void 공연_리뷰_평점_평균_계산_테스트() {
		// Given
		EventHall eventHall = eventHallRepository.save(EventHallFactory.createEventHall());
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));
		Member member = memberRepository.save(MemberFactory.createMember());

		List<EventReviewCreateRequest> requestList = IntStream.range(0, REQUEST_NUMBER)
			.mapToObj(i -> new EventReviewCreateRequest(i, "리뷰 내용 " + i))
			.toList();

		// When
		IntStream.range(0, REQUEST_NUMBER)
			.forEach(i -> eventReviewService.createEventReview(member.getId(), event.getId(), requestList.get(i)));
		Event savedEvent = eventRepository.findById(event.getId()).get();

		// Then
		assertThat(savedEvent.getAverageScore()).isEqualTo(AVERAGE_REVIEW_SCORE);
	}

	@Test
	void 공연_리뷰_수정_테스트() {
		// Given
		EventHall eventHall = eventHallRepository.save(EventHallFactory.createEventHall());
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));
		Member member = memberRepository.save(MemberFactory.createMember());

		EventReview eventReview = eventReviewRepository.save(EventReviewFactory.createEventReview(event, member));

		EventReviewUpdateRequest request = new EventReviewUpdateRequest("공연 후기 수정입니다~!");

		// When
		EventReviewResponse response = eventReviewService.updateEventReview(member.getId(), eventReview.getId(),
			request);

		// Then
		assertThat(response.content()).isEqualTo(request.content());
	}

	@Test
	void 공연_리뷰_아이디로_단건_조회_테스트() {
		// Given
		EventHall eventHall = eventHallRepository.save(EventHallFactory.createEventHall());
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));
		Member member = memberRepository.save(MemberFactory.createMember());

		EventReview eventReview = eventReviewRepository.save(EventReviewFactory.createEventReview(event, member));
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
		EventHall eventHall = eventHallRepository.save(EventHallFactory.createEventHall());
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));
		Member member = memberRepository.save(MemberFactory.createMember());

		IntStream.range(0, REQUEST_NUMBER)
			.forEach(i -> eventReviewRepository.save(new EventReview(i, "리뷰 내용 " + i, event, member)));

		// When
		List<EventReviewResponse> responses = eventReviewService.getEventReviewsForEventByEventId(event.getId());

		// Then
		assertThat(responses).hasSize(REQUEST_NUMBER);
	}

	@Test
	void 공연_리뷰_아이디로_삭제_테스트() {
		// Given
		EventHall eventHall = eventHallRepository.save(EventHallFactory.createEventHall());
		Event event = eventRepository.save(EventFactory.createEvent(eventHall));
		Member member = memberRepository.save(MemberFactory.createMember());

		EventReview eventReview = eventReviewRepository.save(EventReviewFactory.createEventReview(event, member));

		// When
		eventReviewService.deleteEventReviewById(member.getId(), eventReview.getId());

		// Then
		assertThat(eventReviewRepository.findAll()).hasSize(0);
	}
}
