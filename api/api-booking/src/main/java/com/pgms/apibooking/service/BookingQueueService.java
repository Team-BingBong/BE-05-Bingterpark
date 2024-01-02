package com.pgms.apibooking.service;

import org.springframework.stereotype.Service;

import com.pgms.apibooking.dto.request.BookingQueueEnterRequest;
import com.pgms.apibooking.dto.response.OrderInQueueGetResponse;
import com.pgms.apibooking.repository.BookingQueueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingQueueService {

	private final BookingQueueRepository bookingQueueRepository;

	public void enterQueue(BookingQueueEnterRequest request
		, Long memberId) { //TODO: memberId arg 제거, 인증된 memberId 서비스 내에서 접근
		//TODO: 회차 검증
		bookingQueueRepository.add(request.eventTimeId(), memberId);
	}

	public OrderInQueueGetResponse getOrderInQueue(Long timeId
		, Long memberId) { //TODO: memberId arg 제거, 인증된 memberId 서비스 내에서 접근
		Long myOrder = bookingQueueRepository.getQueueSize(timeId, memberId);
		Long entryLimit = bookingQueueRepository.getEntryLimit(timeId);
		Boolean isMyTurn = myOrder <= entryLimit;
		return OrderInQueueGetResponse.of(myOrder, isMyTurn);
	}
}
