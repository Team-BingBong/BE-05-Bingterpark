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

	private final BookingQueueManager bookingQueueManager;
	private final BookingJwtProvider bookingJwtProvider;

	public void enterQueue(BookingQueueEnterRequest request, String sessionId) {
		double currentTimeSeconds = System.currentTimeMillis() / 1000.0;
		bookingQueueManager.add(request.eventId(), sessionId, currentTimeSeconds);
	}

	public OrderInQueueGetResponse getOrderInQueue(Long eventId, String sessionId) {
		Long myOrder = getMyOrder(eventId, sessionId);
		Boolean isMyTurn = isMyTurn(eventId, sessionId);

		double currentTimeSeconds = System.currentTimeMillis() / 1000.0;
		double timeLimitSeconds = currentTimeSeconds - (7 * 60);
		bookingQueueManager.removeRangeByScore(eventId, 0, timeLimitSeconds);

		return OrderInQueueGetResponse.of(myOrder, isMyTurn);
	}

	public TokenIssueResponse issueToken(TokenIssueRequest request, String sessionId) {
		if(!isMyTurn(request.eventId(), sessionId)) {
			throw new BookingException(BookingErrorCode.OUT_OF_ORDER);
		}

		BookingJwtPayload payload = new BookingJwtPayload(sessionId);
		String token = bookingJwtProvider.generateToken(payload);

		return TokenIssueResponse.from(token);
	}

	private Boolean isMyTurn(Long eventId, String sessionId) {
		Long myOrder = getMyOrder(eventId, sessionId);
		Long entryLimit = bookingQueueManager.getEntryLimit();
		return myOrder <= entryLimit;
	}

	public void exitQueue(BookingQueueExitRequest request, String sessionId) {
		bookingQueueManager.remove(request.eventId(), sessionId);
	}

	public SessionIdIssueResponse issueSessionId() {
		UUID sessionId = UUID.randomUUID();
		return SessionIdIssueResponse.from(sessionId.toString());
	}

	private Long getMyOrder(Long eventId, String sessionId) {
		return bookingQueueManager.getRank(eventId, sessionId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.NOT_IN_QUEUE));
	}
}
