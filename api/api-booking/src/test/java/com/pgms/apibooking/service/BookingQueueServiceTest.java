package com.pgms.apibooking.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.pgms.apibooking.dto.request.TokenIssueRequest;
import com.pgms.apibooking.dto.response.OrderInQueueGetResponse;
import com.pgms.apibooking.dto.response.TokenIssueResponse;
import com.pgms.apibooking.repository.BookingQueueRepository;

@SpringBootTest
class BookingQueueServiceTest {

	private static final long EVENT_ID = 0L;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	private BookingQueueService bookingQueueService;

	@Autowired
	private BookingQueueRepository bookingQueueRepository = new BookingQueueRepository(redisTemplate);

	@BeforeEach
	void setUp() {
		for (int i = 0; i < 200; i++) {
			long memberId = i;
			bookingQueueRepository.add(EVENT_ID, memberId);
		}
	}

	@AfterEach
	void tearDown() {
		redisTemplate.delete(String.valueOf(EVENT_ID));
		redisTemplate.delete("waitingNumber:" + EVENT_ID);
	}

	@Test
	void isMyTurn_false() {
		long memberId = 110L;

		OrderInQueueGetResponse response = bookingQueueService.getOrderInQueue(EVENT_ID, memberId);

		assertThat(response.myOrder()).isEqualTo(memberId);
		assertThat(response.isMyTurn()).isFalse();
	}

	@Test
	void isMyTurn_true() {
		long memberId = 110L;

		int completedCount = 50;
		for (int i = 0; i < completedCount; i++) {
			bookingQueueRepository.remove(EVENT_ID, (long)i);
		}

		OrderInQueueGetResponse response = bookingQueueService.getOrderInQueue(EVENT_ID, memberId);

		assertThat(response.myOrder()).isEqualTo(memberId - completedCount);
		assertThat(response.isMyTurn()).isTrue();
	}

	@Test
	void 토큰_발급_테스트() {
		long memberId = 50L;

		TokenIssueRequest request = new TokenIssueRequest(EVENT_ID);
		TokenIssueResponse response = bookingQueueService.issueToken(request, memberId);
		System.out.println(response.token());
	}
}
