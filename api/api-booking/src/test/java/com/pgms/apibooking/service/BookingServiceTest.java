package com.pgms.apibooking.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.config.TestConfig;
import com.pgms.apibooking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.apibooking.factory.EventFactory;
import com.pgms.apibooking.factory.EventHallFactory;
import com.pgms.apibooking.factory.EventSeatAreaFactory;
import com.pgms.apibooking.factory.EventSeatFactory;
import com.pgms.apibooking.factory.EventTimeFactory;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventSeatStatus;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.SeatAreaType;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

@SpringBootTest
@Import(TestConfig.class)
@Transactional
class BookingServiceTest {

	private static final LocalDateTime NOW = LocalDateTime.now();

	@Autowired
	private EventHallRepository eventHallRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventTimeRepository eventTimeRepository;

	@Autowired
	private EventSeatAreaRepository eventSeatAreaRepository;

	@Autowired
	private EventSeatRepository eventSeatRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private BookingService bookingService;

	@Test
	void 예매를_생성한다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW;
		LocalDateTime bookingEndedAt = NOW.plusDays(1);

		EventHall hall = EventHallFactory.generate();
		eventHallRepository.save(hall);

		Event event = EventFactory.generate(
			hall,
			eventStartedAt,
			eventEndedAt,
			bookingStartedAt,
			bookingEndedAt
		);
		eventRepository.save(event);

		EventTime time = EventTimeFactory.generate(event, eventStartedAt, eventEndedAt);
		eventTimeRepository.save(time);

		EventSeatArea area1 = EventSeatAreaFactory.generate(event, SeatAreaType.S);
		eventSeatAreaRepository.save(area1);
		EventSeatArea area2 = EventSeatAreaFactory.generate(event, SeatAreaType.R);
		eventSeatAreaRepository.save(area2);

		String seat1Name = "A1";
		String seat2Name = "A2";

		EventSeat seat1 = EventSeatFactory.generate(time, area1, seat1Name, EventSeatStatus.AVAILABLE);
		eventSeatRepository.save(seat1);
		EventSeat seat2 = EventSeatFactory.generate(time, area2, seat2Name, EventSeatStatus.AVAILABLE);
		eventSeatRepository.save(seat2);

		Long timeId = time.getId();
		List<Long> seatIds = List.of(seat1.getId(), seat2.getId());
		String receiptType = ReceiptType.PICK_UP.getDescription();
		String buyerName = "구매자명";
		String buyerPhoneNumber = "010-1234-5678";

		BookingCreateRequest request = new BookingCreateRequest(
			timeId,
			seatIds,
			receiptType,
			buyerName,
			buyerPhoneNumber,
			Optional.empty()
		);

		// when
		BookingCreateResponse response = bookingService.createBooking(request);

		// then
		Booking booking = bookingRepository.findBookingInfoById(response.bookingId()).get();

		assertThat(booking.getStatus()).isEqualTo(BookingStatus.WAITING_FOR_PAYMENT);
		assertThat(booking.getReceiptType()).isEqualTo(ReceiptType.PICK_UP);
		assertThat(booking.getBuyerName()).isEqualTo(buyerName);
		assertThat(booking.getBuyerPhoneNumber()).isEqualTo(buyerPhoneNumber);
		assertThat(booking.getAmount()).isEqualTo(
			seat1.getEventSeatArea().getPrice() + seat2.getEventSeatArea().getPrice());

		assertThat(booking.getPayment().getAmount()).isEqualTo(booking.getAmount());
		assertThat(booking.getPayment().getStatus()).isEqualTo(PaymentStatus.READY);

		assertThat(booking.getTickets().size()).isEqualTo(2);
		assertThat(booking.getTickets()).extracting("seat").extracting("status").containsOnly(EventSeatStatus.BOOKED);
		assertThat(booking.getTickets()).extracting("seat").extracting("name").containsOnly(seat1Name, seat2Name);
	}

	@Test
	void 예매_기간이_아니면_예매를_생성할_수_없다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW.minusDays(2);
		LocalDateTime bookingEndedAt = NOW.minusDays(1);

		EventHall hall = EventHallFactory.generate();
		eventHallRepository.save(hall);

		Event event = EventFactory.generate(
			hall,
			eventStartedAt,
			eventEndedAt,
			bookingStartedAt,
			bookingEndedAt
		);
		eventRepository.save(event);

		EventTime time = EventTimeFactory.generate(event, eventStartedAt, eventEndedAt);
		eventTimeRepository.save(time);

		EventSeatArea area = EventSeatAreaFactory.generate(event, SeatAreaType.S);
		eventSeatAreaRepository.save(area);

		EventSeat seat = EventSeatFactory.generate(time, area, "A1", EventSeatStatus.AVAILABLE);
		eventSeatRepository.save(seat);

		Long timeId = time.getId();
		List<Long> seatIds = List.of(seat.getId());
		String receiptType = ReceiptType.PICK_UP.getDescription();
		String buyerName = "구매자명";
		String buyerPhoneNumber = "010-1234-5678";

		BookingCreateRequest request = new BookingCreateRequest(
			timeId,
			seatIds,
			receiptType,
			buyerName,
			buyerPhoneNumber,
			Optional.empty()
		);

		// when & then
		assertThatThrownBy(() -> bookingService.createBooking(request))
			.isInstanceOf(BookingException.class)
			.hasMessage(BookingErrorCode.UNBOOKABLE_EVENT.getMessage());
	}

	@Test
	void 선택한_회차의_좌석이_아니면_예매를_생성할_수_없다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW;
		LocalDateTime bookingEndedAt = NOW.plusDays(1);

		EventHall hall = EventHallFactory.generate();
		eventHallRepository.save(hall);

		Event event = EventFactory.generate(
			hall,
			eventStartedAt,
			eventEndedAt,
			bookingStartedAt,
			bookingEndedAt
		);
		eventRepository.save(event);

		EventTime time1 = EventTimeFactory.generate(event, eventStartedAt, eventEndedAt);
		eventTimeRepository.save(time1);
		EventTime time2 = EventTimeFactory.generate(event, eventStartedAt, eventEndedAt);
		eventTimeRepository.save(time2);

		EventSeatArea area = EventSeatAreaFactory.generate(event, SeatAreaType.S);
		eventSeatAreaRepository.save(area);

		EventSeat seat = EventSeatFactory.generate(time1, area, "A1", EventSeatStatus.AVAILABLE);
		eventSeatRepository.save(seat);

		Long timeId = time2.getId();
		List<Long> seatIds = List.of(seat.getId());
		String receiptType = ReceiptType.PICK_UP.getDescription();
		String buyerName = "구매자명";
		String buyerPhoneNumber = "010-1234-5678";

		BookingCreateRequest request = new BookingCreateRequest(
			timeId,
			seatIds,
			receiptType,
			buyerName,
			buyerPhoneNumber,
			Optional.empty()
		);

		// when & then
		assertThatThrownBy(() -> bookingService.createBooking(request))
			.isInstanceOf(BookingException.class)
			.hasMessage(BookingErrorCode.NON_EXISTENT_SEAT_INCLUSION.getMessage());
	}

	@Test
	void 선택한_좌석의_상태가_이미_예매가_완료된_상태라면_예매를_생성할_수_없다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW;
		LocalDateTime bookingEndedAt = NOW.plusDays(1);

		EventHall hall = EventHallFactory.generate();
		eventHallRepository.save(hall);

		Event event = EventFactory.generate(
			hall,
			eventStartedAt,
			eventEndedAt,
			bookingStartedAt,
			bookingEndedAt
		);
		eventRepository.save(event);

		EventTime time = EventTimeFactory.generate(event, eventStartedAt, eventEndedAt);
		eventTimeRepository.save(time);

		EventSeatArea area = EventSeatAreaFactory.generate(event, SeatAreaType.S);
		eventSeatAreaRepository.save(area);

		EventSeat seat = EventSeatFactory.generate(time, area, "A1", EventSeatStatus.BOOKED);
		eventSeatRepository.save(seat);

		Long timeId = time.getId();
		List<Long> seatIds = List.of(seat.getId());
		String receiptType = ReceiptType.PICK_UP.getDescription();
		String buyerName = "구매자명";
		String buyerPhoneNumber = "010-1234-5678";

		BookingCreateRequest request = new BookingCreateRequest(
			timeId,
			seatIds,
			receiptType,
			buyerName,
			buyerPhoneNumber,
			Optional.empty()
		);

		// when & then
		assertThatThrownBy(() -> bookingService.createBooking(request))
			.isInstanceOf(BookingException.class)
			.hasMessage(BookingErrorCode.UNBOOKABLE_SEAT_INCLUSION.getMessage());
	}

	@Test
	void 선택한_수령_방법이_배송인데_배송지_정보가_없다면_예매를_생성할_수_없다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW;
		LocalDateTime bookingEndedAt = NOW.plusDays(1);

		EventHall hall = EventHallFactory.generate();
		eventHallRepository.save(hall);

		Event event = EventFactory.generate(
			hall,
			eventStartedAt,
			eventEndedAt,
			bookingStartedAt,
			bookingEndedAt
		);
		eventRepository.save(event);

		EventTime time = EventTimeFactory.generate(event, eventStartedAt, eventEndedAt);
		eventTimeRepository.save(time);

		EventSeatArea area = EventSeatAreaFactory.generate(event, SeatAreaType.S);
		eventSeatAreaRepository.save(area);

		EventSeat seat = EventSeatFactory.generate(time, area, "A1", EventSeatStatus.AVAILABLE);
		eventSeatRepository.save(seat);

		Long timeId = time.getId();
		List<Long> seatIds = List.of(seat.getId());
		String receiptType = ReceiptType.DELIVERY.getDescription();
		String buyerName = "구매자명";
		String buyerPhoneNumber = "010-1234-5678";

		BookingCreateRequest request = new BookingCreateRequest(
			timeId,
			seatIds,
			receiptType,
			buyerName,
			buyerPhoneNumber,
			Optional.empty()
		);

		// when & then
		assertThatThrownBy(() -> bookingService.createBooking(request))
			.isInstanceOf(BookingException.class)
			.hasMessage(BookingErrorCode.DELIVERY_ADDRESS_REQUIRED.getMessage());
	}
}
