package com.pgms.apibooking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.dto.request.DeliveryAddress;
import com.pgms.apibooking.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.dto.request.RefundAccountRequest;
import com.pgms.apibooking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.TicketRepository;
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
	private final TicketRepository ticketRepository;
	private final TossPaymentConfig tossPaymentConfig;
	private final PaymentService paymentService;

	public BookingCreateResponse createBooking(BookingCreateRequest request) {
		EventTime time = getBookableTimeWithEvent(request.timeId());
		List<EventSeat> seats = getBookableSeatsWithArea(request.timeId(), request.seatIds());

		ReceiptType receiptType = ReceiptType.fromDescription(request.receiptType());
		validateDeliveryAddress(receiptType, request.deliveryAddress());

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

	public void cancelBooking(String id, BookingCancelRequest request) {
		Booking booking = bookingRepository.findBookingInfoById(id)
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));

		if (!booking.isCancelable()) {
			throw new BookingException(BookingErrorCode.UNCANCELABLE_BOOKING);
		}

		validateRefundReceiveAccount(booking.getPayment().getMethod(), request.refundReceiveAccount());

		paymentService.cancelPayment(
			booking.getPayment().getPaymentKey(),
			PaymentCancelRequest.of(
				request.cancelReason(),
				booking.isPaid() ? booking.getAmount() : 0,
				request.refundReceiveAccount()
			)
		);

		booking.cancel(
			BookingCancelRequest.toEntity(request, "사용자", booking) //TODO: 취소 요청자 지정
		);
	}

	public void exitBooking(String id) {
		List<Ticket> ticketsWithSeat = ticketRepository.findAllByBookingId(id);
		ticketsWithSeat.forEach(ticket -> ticket.getSeat().updateStatus(EventSeatStatus.AVAILABLE));
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

	private void validateDeliveryAddress(ReceiptType receiptType, Optional<DeliveryAddress> deliveryAddress) {
		if (receiptType == ReceiptType.DELIVERY) {
			if (deliveryAddress.isEmpty()) {
				throw new BookingException(BookingErrorCode.DELIVERY_ADDRESS_REQUIRED);
			}
		}
	}

	private void validateRefundReceiveAccount(PaymentMethod paymentMethod, Optional<RefundAccountRequest> refundReceiveAccount) {
		if (paymentMethod == PaymentMethod.VIRTUAL_ACCOUNT && refundReceiveAccount.isEmpty()) {
			throw new BookingException(BookingErrorCode.REFUND_ACCOUNT_REQUIRED);
		}
	}
}
