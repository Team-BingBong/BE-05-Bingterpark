package com.pgms.apibooking.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pgms.apibooking.dto.request.BookingQueueEnterRequest;
import com.pgms.apibooking.dto.request.BookingQueueExitRequest;
import com.pgms.apibooking.dto.request.TokenIssueRequest;
import com.pgms.apibooking.dto.response.OrderInQueueGetResponse;
import com.pgms.apibooking.dto.response.SessionIdIssueResponse;
import com.pgms.apibooking.dto.response.TokenIssueResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.apibooking.jwt.BookingJwtPayload;
import com.pgms.apibooking.jwt.BookingJwtProvider;
import com.pgms.apibooking.repository.BookingQueueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingQueueService {

	private final BookingQueueRepository bookingQueueRepository;
	private final BookingJwtProvider bookingJwtProvider;

	public void enterQueue(BookingQueueEnterRequest request, String sessionId) {
		bookingQueueRepository.add(request.eventId(), sessionId);
	}

	public OrderInQueueGetResponse getOrderInQueue(Long eventId, String sessionId) {
		Long myOrder = bookingQueueRepository.getRank(eventId, sessionId);
		Boolean isMyTurn = isMyTurn(eventId, sessionId);
		return OrderInQueueGetResponse.of(myOrder, isMyTurn);
	}

	public TokenIssueResponse issueToken(TokenIssueRequest request, String sessionId) {
		if(!isMyTurn(request.eventId(), sessionId)) {
			throw new BookingException(BookingErrorCode.OUT_OF_ORDER);
		}

		BookingJwtPayload payload = new BookingJwtPayload(sessionId);
		String token = bookingJwtProvider.generateToken(payload);

		bookingQueueRepository.remove(request.eventId(), sessionId);

		return TokenIssueResponse.from(token);
	}

	private Boolean isMyTurn(Long eventId, String sessionId) {
		Long myOrder = bookingQueueRepository.getRank(eventId, sessionId);
		Long entryLimit = bookingQueueRepository.getEntryLimit();
		return myOrder <= entryLimit;
	}

	public void exitQueue(BookingQueueExitRequest request, String sessionId) {
		bookingQueueRepository.remove(request.eventId(), sessionId);
	}

	public SessionIdIssueResponse issueSessionId() {
		UUID sessionId = UUID.randomUUID();
		return SessionIdIssueResponse.from(sessionId.toString());
	}
}
