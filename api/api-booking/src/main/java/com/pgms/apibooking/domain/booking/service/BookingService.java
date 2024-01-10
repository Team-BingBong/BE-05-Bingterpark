package com.pgms.apibooking.domain.booking.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.domain.booking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.domain.booking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.domain.booking.dto.request.BookingSearchCondition;
import com.pgms.apibooking.domain.booking.dto.request.DeliveryAddress;
import com.pgms.apibooking.domain.booking.dto.request.PageCondition;
import com.pgms.apibooking.domain.booking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.domain.booking.dto.response.BookingGetResponse;
import com.pgms.apibooking.domain.booking.dto.response.BookingsGetResponse;
import com.pgms.apibooking.domain.booking.dto.response.PageResponse;
import com.pgms.apibooking.domain.booking.repository.BookingQuerydslRepository;
import com.pgms.apibooking.domain.bookingqueue.repository.BookingQueueRepository;
import com.pgms.apibooking.domain.payment.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.domain.payment.dto.request.RefundAccountRequest;
import com.pgms.apibooking.domain.payment.service.PaymentService;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.Ticket;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.TicketRepository;
import com.pgms.coredomain.domain.common.BookingErrorCode;
import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService { //TODO: 테스트 코드 작성

	private final EventTimeRepository eventTimeRepository;
	private final EventSeatRepository eventSeatRepository;
	private final BookingRepository bookingRepository;
	private final TicketRepository ticketRepository;
	private final MemberRepository memberRepository;
	private final BookingQuerydslRepository bookingQuerydslRepository;
	private final BookingQueueRepository bookingQueueRepository;
	private final TossPaymentConfig tossPaymentConfig;
	private final PaymentService paymentService;

	public BookingCreateResponse createBooking(BookingCreateRequest request, Long memberId, String tokenSessionId) {
		Member member = getMemberById(memberId);
		EventTime time = getBookableTimeWithEvent(request.timeId());
		List<EventSeat> seats = getBookableSeatsWithArea(request.timeId(), request.seatIds());

		ReceiptType receiptType = ReceiptType.fromDescription(request.receiptType());
		validateDeliveryAddress(receiptType, request.deliveryAddress());

		Booking booking = BookingCreateRequest.toEntity(request, time, seats, member);

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

		removeSessionIdInBookingQueue(booking.getTime().getEvent().getId(), tokenSessionId);

		return BookingCreateResponse.of(booking, tossPaymentConfig.getSuccessUrl(), tossPaymentConfig.getFailUrl());
	}

	public void cancelBooking(String id, BookingCancelRequest request, Long memberId) {
		Member member = getMemberById(memberId);
		Booking booking = bookingRepository.findBookingInfoById(id)
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));

		if (!booking.isSameBooker(memberId)) {
			throw new BookingException(BookingErrorCode.FORBIDDEN);
		}

		if (!booking.isCancelable()) {
			throw new BookingException(BookingErrorCode.UNCANCELABLE_BOOKING);
		}

		validateRefundReceiveAccount(booking.getPayment().getMethod(), request.refundReceiveAccount());

		int cancelAmount = booking.isPaid() ? booking.getAmount() : 0;

		paymentService.cancelPayment(
			booking.getPayment().getPaymentKey(),
			PaymentCancelRequest.of(
				request.cancelReason(),
				cancelAmount,
				request.refundReceiveAccount()
			)
		);

		booking.cancel(
			BookingCancelRequest.toEntity(request, cancelAmount, member.getEmail(), booking)
		);
	}

	public void exitBooking(String id) {
		List<Ticket> ticketsWithSeat = ticketRepository.findAllByBookingId(id);
		ticketsWithSeat.forEach(ticket -> ticket.getSeat().updateStatus(EventSeatStatus.AVAILABLE));
	}

	@Transactional(readOnly = true)
	public PageResponse<BookingsGetResponse> getBookings(
		PageCondition pageCondition,
		BookingSearchCondition searchCondition,
		Long memberId
	) {
		Pageable pageable = PageRequest.of(pageCondition.getPage() - 1, pageCondition.getSize());
		searchCondition.updateMemberId(memberId);

		List<BookingsGetResponse> bookings = bookingQuerydslRepository.findAll(searchCondition, pageable)
			.stream()
			.map(BookingsGetResponse::from)
			.toList();

		return PageResponse.of(
			PageableExecutionUtils.getPage(bookings, pageable, () -> bookingQuerydslRepository.count(searchCondition))
		);
	}

	@Transactional(readOnly = true)
	public BookingGetResponse getBooking(String id, Long memberId) {
		Booking booking = bookingRepository.findBookingInfoById(id)
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));

		if (!booking.isSameBooker(memberId)) {
			throw new BookingException(BookingErrorCode.FORBIDDEN);
		}

		return BookingGetResponse.from(booking);
	}

	@Async
	protected void removeSessionIdInBookingQueue(Long eventId, String tokenSessionId) {
		bookingQueueRepository.remove(eventId, tokenSessionId);
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

	private void validateRefundReceiveAccount(PaymentMethod paymentMethod,
		Optional<RefundAccountRequest> refundReceiveAccount) {
		if (paymentMethod == PaymentMethod.VIRTUAL_ACCOUNT && refundReceiveAccount.isEmpty()) {
			throw new BookingException(BookingErrorCode.REFUND_ACCOUNT_REQUIRED);
		}
	}

	private Member getMemberById(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new BookingException(MemberErrorCode.MEMBER_NOT_FOUND));
	}
}
