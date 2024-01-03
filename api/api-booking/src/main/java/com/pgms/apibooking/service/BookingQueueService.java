package com.pgms.apibooking.service;

import java.awt.print.Book;

import org.springframework.stereotype.Service;

import com.pgms.apibooking.dto.request.BookingQueueEnterRequest;
import com.pgms.apibooking.dto.response.OrderInQueueGetResponse;
import com.pgms.apibooking.dto.response.TokenIssueResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.apibooking.jwt.JwtPayload;
import com.pgms.apibooking.jwt.JwtProvider;
import com.pgms.apibooking.repository.BookingQueueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingQueueService {

	private final BookingQueueRepository bookingQueueRepository;
	private final JwtProvider jwtProvider;

	public void enterQueue(BookingQueueEnterRequest request
		, Long memberId) { //TODO: memberId arg 제거, 인증된 memberId 서비스 내에서 접근
		//TODO: 회차 검증
		bookingQueueRepository.add(request.eventTimeId(), memberId);
	}

	public OrderInQueueGetResponse getOrderInQueue(Long timeId
		, Long memberId) { //TODO: memberId arg 제거, 인증된 memberId 서비스 내에서 접근
		Long myOrder = bookingQueueRepository.getRank(timeId, memberId);
		Boolean isMyTurn = isMyTurn(timeId, memberId);
		return OrderInQueueGetResponse.of(myOrder, isMyTurn);
	}

	public TokenIssueResponse issueToken(BookingQueueEnterRequest request
		, Long memberId) { //TODO: memberId arg 제거, 인증된 memberId 서비스 내에서 접근
		if(!isMyTurn(request.eventTimeId(), memberId)) {
			throw new BookingException(BookingErrorCode.OUT_OF_ORDER);
		}

		JwtPayload payload = new JwtPayload(memberId);
		String token = jwtProvider.generateToken(payload);

		bookingQueueRepository.remove(request.eventTimeId(), memberId);

		return TokenIssueResponse.from(token);
	}

	private Boolean isMyTurn(Long timeId, Long memberId) {
		Long myOrder = bookingQueueRepository.getRank(timeId, memberId);
		Long entryLimit = bookingQueueRepository.getEntryLimit();
		return myOrder <= entryLimit;
	}
}
