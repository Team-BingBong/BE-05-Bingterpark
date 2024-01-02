package com.pgms.apibooking.service;

import org.springframework.stereotype.Service;

import com.pgms.apibooking.dto.request.BookingQueueEnterRequest;
import com.pgms.apibooking.repository.BookingQueueRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingQueueService {

	private final BookingQueueRepository bookingQueueRepository;

	public void enterBookingQueue(BookingQueueEnterRequest request) {
		bookingQueueRepository.add(request.eventTimeId(), 1L); //TODO: 인증된 memberId
	}
}
