package com.pgms.apibooking.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.factory.EventFactory;
import com.pgms.apibooking.factory.EventHallFactory;
import com.pgms.apibooking.factory.EventSeatAreaFactory;
import com.pgms.apibooking.factory.EventSeatFactory;
import com.pgms.apibooking.factory.EventTimeFactory;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.EventSeat;
import com.pgms.coredomain.domain.event.EventSeatArea;
import com.pgms.coredomain.domain.event.EventTime;
import com.pgms.coredomain.domain.event.SeatAreaType;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;
import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
import com.pgms.coredomain.domain.event.repository.EventTimeRepository;

@SpringBootTest
@Transactional
class BookingServiceTest {

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

	private static final LocalDateTime NOW = LocalDateTime.now();

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

		EventSeatArea area = EventSeatAreaFactory.generate(event, SeatAreaType.S);
		eventSeatAreaRepository.save(area);

		String seat1Name = "A1";
		String seat2Name = "A2";

		EventSeat seat1 = EventSeatFactory.generate(time, area, seat1Name);
		eventSeatRepository.save(seat1);
		EventSeat seat2 = EventSeatFactory.generate(time, area, seat2Name);
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
		assertThat(booking.getReceiptType()).isEqualTo(ReceiptType.PICK_UP);
		assertThat(booking.getBuyerName()).isEqualTo(buyerName);
		assertThat(booking.getBuyerPhoneNumber()).isEqualTo(buyerPhoneNumber);
		//TODO: 상태 확인
	}

	@Test
	void 예매_기간이_아니면_예매를_생성할_수_없다() {
		// given

		// when

		// then
	}
}
