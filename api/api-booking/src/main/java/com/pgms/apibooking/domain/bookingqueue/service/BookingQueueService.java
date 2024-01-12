package com.pgms.apibooking.domain.bookingqueue.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.common.jwt.BookingJwtPayload;
import com.pgms.apibooking.common.jwt.BookingJwtProvider;
import com.pgms.apibooking.domain.bookingqueue.dto.request.BookingQueueEnterRequest;
import com.pgms.apibooking.domain.bookingqueue.dto.request.BookingQueueExitRequest;
import com.pgms.apibooking.domain.bookingqueue.dto.request.TokenIssueRequest;
import com.pgms.apibooking.domain.bookingqueue.dto.response.OrderInQueueGetResponse;
import com.pgms.apibooking.domain.bookingqueue.dto.response.SessionIdIssueResponse;
import com.pgms.apibooking.domain.bookingqueue.dto.response.TokenIssueResponse;
import com.pgms.coredomain.domain.common.BookingErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingQueueService {

	private final static double MILLISECONDS_PER_SECOND = 1000.0;
	private final static double TIMEOUT_SECONDS = 7 * 60;
	private final static long ENTRY_LIMIT = 5;

	private final BookingQueueManager bookingQueueManager;
	private final BookingJwtProvider bookingJwtProvider;

	public void enterQueue(BookingQueueEnterRequest request, String sessionId) {
		double currentTimeSeconds = System.currentTimeMillis() / MILLISECONDS_PER_SECOND;
		bookingQueueManager.add(request.eventId(), sessionId, currentTimeSeconds);
	}

	public OrderInQueueGetResponse getOrderInQueue(Long eventId, String sessionId) {
		Long order = getOrder(eventId, sessionId);
		Long myOrder = order <= ENTRY_LIMIT ? 0 : order - ENTRY_LIMIT;
		Boolean isMyTurn = isReadyToEnter(eventId, sessionId);

		double currentTimeSeconds = System.currentTimeMillis() / MILLISECONDS_PER_SECOND;
		double timeLimitSeconds = currentTimeSeconds - TIMEOUT_SECONDS;
		bookingQueueManager.removeRangeByScore(eventId, 0, timeLimitSeconds);

		return OrderInQueueGetResponse.of(myOrder, isMyTurn);
	}

	public TokenIssueResponse issueToken(TokenIssueRequest request, String sessionId) {
		if (!isReadyToEnter(request.eventId(), sessionId)) {
			throw new BookingException(BookingErrorCode.OUT_OF_ORDER);
		}

		BookingJwtPayload payload = new BookingJwtPayload(sessionId);
		String token = bookingJwtProvider.generateToken(payload);

		return TokenIssueResponse.from(token);
	}

	private Long getOrder(Long eventId, String sessionId) {
		return bookingQueueManager.getRank(eventId, sessionId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.NOT_IN_QUEUE));
	}

	private Boolean isReadyToEnter(Long eventId, String sessionId) {
		Long myOrder = getOrder(eventId, sessionId);
		Long entryLimit = ENTRY_LIMIT;
		return myOrder <= entryLimit;
	}

	public void exitQueue(BookingQueueExitRequest request, String sessionId) {
		bookingQueueManager.remove(request.eventId(), sessionId);
	}

	public SessionIdIssueResponse issueSessionId() {
		UUID sessionId = UUID.randomUUID();
		return SessionIdIssueResponse.from(sessionId.toString());
	}
}
