package com.pgms.apibooking.service;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import com.pgms.apibooking.domain.bookingqueue.dto.request.TokenIssueRequest;
import com.pgms.apibooking.domain.bookingqueue.dto.response.TokenIssueResponse;
import com.pgms.apibooking.domain.bookingqueue.service.BookingQueueService;
import com.pgms.apibooking.domain.bookingqueue.repository.BookingQueueRepository;

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
			String  sessionId = UUID.randomUUID().toString();
			bookingQueueRepository.add(EVENT_ID, sessionId);
		}
	}

	@AfterEach
	void tearDown() {
		redisTemplate.delete(String.valueOf(EVENT_ID));
		redisTemplate.delete("waitingNumber:" + EVENT_ID);
	}

	@Test
	void 토큰_발급_테스트() {
		// given
		String sessionId = UUID.randomUUID().toString();
		TokenIssueRequest request = new TokenIssueRequest(EVENT_ID);

		// when
		TokenIssueResponse response = bookingQueueService.issueToken(request, sessionId);

		// then
		System.out.println(response.token());
	}
}
