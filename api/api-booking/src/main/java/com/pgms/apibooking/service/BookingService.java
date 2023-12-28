package com.pgms.apibooking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.dto.request.PaymentCreateRequest;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.Ticket;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;
import com.pgms.coredomain.domain.event.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;
	private final TicketRepository ticketRepository;
	private final EventTimeRepository eventTimeRepository;
	private final EventSeatRepository eventSeatRepository;

	//TODO: 리팩터링
	//TODO: 테스트 코드 작성
	public Booking generateBooking(PaymentCreateRequest bookingInfo) {
		// 결제할 좌석이 담겨져 왔는지 확인
		if (bookingInfo.seatIds().isEmpty()) {
			throw new BookingException(BookingErrorCode.SEAT_SELECTION_REQUIRED);
		}

		// 존재하는 공연 회차인지 확인
		EventTime eventTime = eventTimeRepository.findEventTimeWithEventById(bookingInfo.eventTimeId())
			.orElseThrow(() -> new BookingException(BookingErrorCode.EVENT_TIME_NOT_FOUND));

		// 예매가능한 공연 회차인지 확인
		if (!eventTime.isBookingAvailable()) {
			throw new BookingException(BookingErrorCode.BOOKING_UNAVAILABLE);
		}

		// 요청한 공연 회차의 예매 가능한 좌석인지 확인
		List<EventSeat> seats = eventSeatRepository
			.findAllWithAreaByEventTimeIdAndSeatIdsAndStatus(bookingInfo.eventTimeId(), bookingInfo.seatIds(),
				EventSeatStatus.BEING_BOOKED);

		if (seats.size() != bookingInfo.seatIds().size()) {
			throw new BookingException(BookingErrorCode.EVENT_TIME_SEAT_MISMATCH);
		}

		// 수령 방법이 배송이라면 배송지 정보가 있는지 확인
		if (bookingInfo.receiptType() == ReceiptType.DELIVERY) {
			if (bookingInfo.deliveryAddress().isEmpty()) {
				throw new BookingException(BookingErrorCode.DELIVERY_ADDRESS_REQUIRED);
			}
		}

		// 예매 정보 저장
		Booking booking = Booking.builder()
			.id(String.valueOf(System.currentTimeMillis()))
			.bookingName(eventTime.getEvent().getTitle() + " " + eventTime.getRound() + " ")
			.status(BookingStatus.WAITING_FOR_DEPOSIT)
			.receiptType(bookingInfo.receiptType())
			.buyerName(bookingInfo.buyerName())
			.buyerPhoneNumber(bookingInfo.buyerPhoneNumber())
			.recipientName(bookingInfo.deliveryAddress().get().recipientName())
			.recipientPhoneNumber(bookingInfo.deliveryAddress().get().recipientPhoneNumber())
			.streetAddress(bookingInfo.deliveryAddress().get().streetAddress())
			.detailAddress(bookingInfo.deliveryAddress().get().detailAddress())
			.amount(seats.stream()
				.map(seat -> seat.getEventSeatArea().getPrice())
				.reduce(0, Integer::sum))
			.member(null) //TODO: 인증된 멤버 지정
			.build();

		bookingRepository.save(booking);

		// 티켓 정보 저장
		List<Ticket> tickets = seats.stream()
			.map(seat -> Ticket.builder()
				.eventSeat(seat)
				.booking(booking)
				.build())
			.toList();

		ticketRepository.saveAll(tickets);

		// 좌석 상태 변경
		seats.forEach(seat -> seat.updateStatus(EventSeatStatus.BOOKED));

		return booking;
	}

}
