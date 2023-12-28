package com.pgms.apibooking.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.dto.request.DeliveryAddress;
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

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookingService {

	private final BookingRepository bookingRepository;
	private final EventTimeRepository eventTimeRepository;
	private final EventSeatRepository eventSeatRepository;

	//TODO: 테스트 코드 작성
	public Booking createBooking(PaymentCreateRequest request) {
		EventTime time = getBookableTimeWithEvent(request.timeId());
		List<EventSeat> seats = geBookableSeatsWithArea(request.timeId(), request.seatIds());
		validateDeliveryAddress(request.receiptType(), request.deliveryAddress().orElse(null));

		Booking booking = Booking.builder()
			.id(String.valueOf(System.currentTimeMillis()))
			.bookingName(time.getEvent().getTitle() + " " + time.getRound())
			.status(BookingStatus.WAITING_FOR_DEPOSIT)
			.receiptType(request.receiptType())
			.buyerName(request.buyerName())
			.buyerPhoneNumber(request.buyerPhoneNumber())
			.recipientName(request.deliveryAddress().isPresent()
				? request.deliveryAddress().get().recipientName() : null)
			.recipientPhoneNumber(request.deliveryAddress().isPresent()
				? request.deliveryAddress().get().recipientPhoneNumber() : null)
			.streetAddress(request.deliveryAddress().isPresent()
				? request.deliveryAddress().get().streetAddress() : null)
			.detailAddress(request.deliveryAddress().isPresent()
				? request.deliveryAddress().get().detailAddress() : null)
			.amount(seats.stream()
				.map(seat -> seat.getEventSeatArea().getPrice())
				.reduce(0, Integer::sum))
			.member(null) //TODO: 인증된 멤버 지정
			.build();

		seats.forEach(seat -> booking.addTicket(Ticket.builder()
			.eventSeat(seat)
			.booking(booking)
			.build()))
		;

		bookingRepository.save(booking);

		seats.forEach(seat -> seat.updateStatus(EventSeatStatus.BOOKED));

		return booking;
	}

	private EventTime getBookableTimeWithEvent(Long timeId) {
		EventTime time = eventTimeRepository.findEventTimeWithEventById(timeId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.TIME_NOT_FOUND));

		if (!time.getEvent().isBookable()) {
			throw new BookingException(BookingErrorCode.UNBOOKABLE_EVENT);
		}

		return time;
	}

	private List<EventSeat> geBookableSeatsWithArea(Long timeId, List<Long> seatIds) {
		List<EventSeat> seats = eventSeatRepository
			.findAllWithAreaByTimeIdAndSeatIds(timeId, seatIds);

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
