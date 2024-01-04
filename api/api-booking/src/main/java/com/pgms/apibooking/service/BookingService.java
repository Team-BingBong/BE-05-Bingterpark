package com.pgms.apibooking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.dto.request.DeliveryAddress;
import com.pgms.apibooking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.booking.Ticket;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService { //TODO: 테스트 코드 작성

	private final EventTimeRepository eventTimeRepository;
	private final EventSeatRepository eventSeatRepository;
	private final BookingRepository bookingRepository;
	private final TossPaymentConfig tossPaymentConfig;
	private final PaymentService paymentService;

	public BookingCreateResponse createBooking(BookingCreateRequest request) {
		EventTime time = getBookableTimeWithEvent(request.timeId());
		List<EventSeat> seats = getBookableSeatsWithArea(request.timeId(), request.seatIds());

		ReceiptType receiptType = ReceiptType.fromDescription(request.receiptType());
		validateDeliveryAddress(receiptType, request.deliveryAddress().orElse(null));

		Booking booking = BookingCreateRequest.toEntity(request, time, seats, null); //TODO: 인증된 멤버 지정

		seats.forEach(seat -> booking.addTicket(
			Ticket.builder()
				.seat(seat)
				.build())
		);

		booking.updatePayment(
			Payment.builder()
				.amount(booking.getAmount())
				.status(PaymentStatus.READY)
				.build()
		);

		bookingRepository.save(booking);

		seats.forEach(seat -> seat.updateStatus(EventSeatStatus.BOOKED));

		return BookingCreateResponse.of(booking, tossPaymentConfig.getSuccessUrl(), tossPaymentConfig.getFailUrl());
	}

	public void cancelBooking(String id, String paymentKey, BookingCancelRequest request) {
		Booking booking = bookingRepository.findBookingInfoById(id)
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));

		if (!booking.isCancelable()) {
			throw new BookingException(BookingErrorCode.UNCANCELABLE_BOOKING);
		}

		paymentService.cancelPayment(paymentKey, request);

		booking.cancel(
			BookingCancelRequest.toEntity(request, "사용자", booking) //TODO: 취소 요청자 지정
		);
	}

	private EventTime getBookableTimeWithEvent(Long timeId) {
		EventTime time = eventTimeRepository.findWithEventById(timeId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.TIME_NOT_FOUND));

		if (!time.getEvent().isBookable()) {
			throw new BookingException(BookingErrorCode.UNBOOKABLE_EVENT);
		}

		return time;
	}

	private List<EventSeat> getBookableSeatsWithArea(Long timeId, List<Long> seatIds) {
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
}
