package com.pgms.apibooking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.dto.request.DeliveryAddress;
import com.pgms.apibooking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.repository.BookingCancelRepository;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {

	private final EventTimeRepository eventTimeRepository;
	private final EventSeatRepository eventSeatRepository;
	private final BookingRepository bookingRepository;
	private final BookingCancelRepository bookingCancelRepository;
	private final TossPaymentConfig tossPaymentConfig;

	//TODO: 테스트 코드 작성
	public BookingCreateResponse createBooking(BookingCreateRequest request) {
		EventTime time = getBookableTimeWithEvent(request.timeId());
		List<EventSeat> seats = geBookableSeatsWithArea(request.timeId(), request.seatIds());
		validateDeliveryAddress(request.receiptType(), request.deliveryAddress().orElse(null));

		Booking booking = request.toEntity(time, seats, null); //TODO: 인증된 멤버 지정
		bookingRepository.save(booking);

		seats.forEach(seat -> seat.updateStatus(EventSeatStatus.BOOKED));

		return BookingCreateResponse.of(booking, tossPaymentConfig.getSuccessUrl(), tossPaymentConfig.getFailUrl());
	}

	private EventTime getBookableTimeWithEvent(Long timeId) {
		EventTime time = eventTimeRepository.findWithEventById(timeId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.TIME_NOT_FOUND));

		if (!time.getEvent().isBookable()) {
			throw new BookingException(BookingErrorCode.UNBOOKABLE_EVENT);
		}

		return time;
	}

	private List<EventSeat> geBookableSeatsWithArea(Long timeId, List<Long> seatIds) {
		List<EventSeat> seats = eventSeatRepository.findAllWithAreaByTimeIdAndSeatIds(timeId, seatIds);

		if (seats.size() != seatIds.size()) {
			throw new BookingException(BookingErrorCode.NON_EXISTENT_SEAT_INCLUSION);
		}

		if (seats.stream().anyMatch(EventSeat::isBooked)) {
			throw new BookingException(BookingErrorCode.UNBOOKABLE_SEAT_INCLUSION);
		}

		return seats;
	}

	private void validateDeliveryAddress(ReceiptType receiptType, DeliveryAddress deliveryAddress) {
		if (receiptType == ReceiptType.DELIVERY) {
			if (deliveryAddress == null) {
				throw new BookingException(BookingErrorCode.DELIVERY_ADDRESS_REQUIRED);
			}
		}
	}

	// TODO
	public void cancelBooking(String id) {
		// 취소 가능한 상태인지 확인: 공연 시작일 전인지, 결제 완료 상태인지
		// 결제 취소
		// 상태 변경
	}
}
