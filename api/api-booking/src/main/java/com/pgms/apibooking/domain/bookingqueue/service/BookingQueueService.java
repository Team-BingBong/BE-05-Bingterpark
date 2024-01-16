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
	private final static long WAITING_QUEUE_TIMEOUT_SECONDS = 2 * 60 * 60; // 2 hours
	private final static long PARTICIPANT_QUEUE_TIMEOUT_SECONDS = 7 * 60; // 7 minutes
	private final static int ENTRY_LIMIT = 2;

	private final BookingQueueManager bookingQueueManager;
	private final BookingJwtProvider bookingJwtProvider;

	public SessionIdIssueResponse issueSessionId() {
		UUID sessionId = UUID.randomUUID();
		return SessionIdIssueResponse.from(sessionId.toString());
	}

	public void enterWaitingQueue(BookingQueueEnterRequest request, String sessionId) {
		double currentTimeSeconds = System.currentTimeMillis() / MILLISECONDS_PER_SECOND;
		bookingQueueManager.addToWaitingQueue(request.eventId(), sessionId, currentTimeSeconds);
	}

	public OrderInQueueGetResponse getWaitingOrder(Long eventId, String sessionId) {
		cleanQueue(eventId);

		Long myOrder = getOrderInWaitingQueue(eventId, sessionId);
		Boolean isMyTurn = myOrder <= getAvailableEntryCountForParticipantQueue(eventId);

		if (isMyTurn) {
			bookingQueueManager.removeFromWaitingQueue(eventId, sessionId);
			double currentTimeSeconds = System.currentTimeMillis() / MILLISECONDS_PER_SECOND;
			bookingQueueManager.addToParticipantQueue(eventId, sessionId, currentTimeSeconds);
		}

		return OrderInQueueGetResponse.of(myOrder, isMyTurn);
	}

	public TokenIssueResponse issueToken(TokenIssueRequest request, String sessionId) {
		if (!existsParticipant(request.eventId(), sessionId)) {
			throw new BookingException(BookingErrorCode.OUT_OF_ORDER);
		}

		BookingJwtPayload payload = new BookingJwtPayload(sessionId);
		String token = bookingJwtProvider.generateToken(payload);
		return TokenIssueResponse.from(token);
	}

	public void exitQueue(BookingQueueExitRequest request, String sessionId) {
		bookingQueueManager.removeFromWaitingQueue(request.eventId(), sessionId);
		bookingQueueManager.removeFromParticipantQueue(request.eventId(), sessionId);
	}

	private Long getOrderInWaitingQueue(Long eventId, String sessionId) {
		Long rank = bookingQueueManager.getRankInWaitingQueue(eventId, sessionId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.NOT_IN_QUEUE));
		return rank + 1;
	}

	private Long getAvailableEntryCountForParticipantQueue(Long eventId) {
		Long participantCount = bookingQueueManager.getSizeOfParticipantQueue(eventId);
		return ENTRY_LIMIT - participantCount;
	}

	private Boolean existsParticipant(Long eventId, String sessionId) {
		return bookingQueueManager.getElementScore(eventId, sessionId) != null;
	}

	/*
	 * 대기열, 참가열에 존재하는 세션 중 타임아웃된 세션을 제거한다.
	 */
	private void cleanQueue(Long eventId) {
		double currentTimeSeconds = System.currentTimeMillis() / MILLISECONDS_PER_SECOND;
		bookingQueueManager.removeRangeByScoreFromWaitingQueue(
			eventId,
			0,
			currentTimeSeconds - WAITING_QUEUE_TIMEOUT_SECONDS
		);
		bookingQueueManager.removeRangeByScoreFromParticipantQueue(
			eventId,
			0,
			currentTimeSeconds - PARTICIPANT_QUEUE_TIMEOUT_SECONDS
		);
	}
}
