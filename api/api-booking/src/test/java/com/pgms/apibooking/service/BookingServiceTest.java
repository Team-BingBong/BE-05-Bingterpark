package com.pgms.apibooking.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.common.exception.BookingErrorCode;
import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.config.TestConfig;
import com.pgms.apibooking.domain.booking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.domain.booking.dto.request.BookingCreateRequest;
import com.pgms.apibooking.domain.booking.dto.response.BookingCreateResponse;
import com.pgms.apibooking.domain.booking.service.BookingService;
import com.pgms.apibooking.domain.bookingqueue.repository.BookingQueueRepository;
import com.pgms.apibooking.domain.payment.dto.request.RefundAccountRequest;
import com.pgms.apibooking.factory.BookingFactory;
import com.pgms.apibooking.factory.EventFactory;
import com.pgms.apibooking.factory.EventHallFactory;
import com.pgms.apibooking.factory.EventSeatAreaFactory;
import com.pgms.apibooking.factory.EventSeatFactory;
import com.pgms.apibooking.factory.EventTimeFactory;
import com.pgms.apibooking.factory.PaymentFactory;
import com.pgms.apibooking.factory.TicketFactory;
import com.pgms.coredomain.domain.booking.BankCode;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.Ticket;
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
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.enums.Provider;
import com.pgms.coredomain.domain.member.repository.MemberRepository;

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
	
	@Autowired
	private MemberRepository memberRepository;

	@MockBean
	private BookingQueueRepository bookingQueueRepository;

	private Member member;

	@BeforeEach
	void setup() {
		member = memberRepository.save(Member.builder()
			.email("test@gmail.com")
			.password("test1234!")
			.name("홍길동")
			.provider(Provider.KAKAO)
			.phoneNumber("010-123-456")
			.build());
	}

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
		String buyerName = "구매자 명";
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
		BookingCreateResponse response = bookingService.createBooking(request, member.getId());

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
		assertThat(booking.getTickets())
			.extracting("seat")
			.extracting("status")
			.containsOnly(EventSeatStatus.BOOKED);
		assertThat(booking.getTickets())
			.extracting("seat")
			.extracting("name")
			.containsOnly(seat1Name, seat2Name);
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
		String buyerName = "구매자 명";
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
		assertThatThrownBy(() -> bookingService.createBooking(request, member.getId()))
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
		String buyerName = "구매자 명";
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
		assertThatThrownBy(() -> bookingService.createBooking(request, member.getId()))
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
		String buyerName = "구매자 명";
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
		assertThatThrownBy(() -> bookingService.createBooking(request, member.getId()))
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
		String buyerName = "구매자 명";
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
		assertThatThrownBy(() -> bookingService.createBooking(request, member.getId()))
			.isInstanceOf(BookingException.class)
			.hasMessage(BookingErrorCode.DELIVERY_ADDRESS_REQUIRED.getMessage());
	}

	@Test
	void 예매를_취소한다() {
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

		Booking booking = BookingFactory.generate(
			member,
			time,
			seat.getEventSeatArea().getPrice(),
			BookingStatus.PAYMENT_COMPLETED
		);
		Ticket ticket = TicketFactory.generate(seat);
		booking.addTicket(ticket);
		Payment payment = PaymentFactory.generate(PaymentMethod.CARD, booking.getAmount(), PaymentStatus.DONE);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		String cancelReason = "단순 변심";

		BookingCancelRequest request = new BookingCancelRequest(
			cancelReason,
			Optional.empty()
		);

		// when
		bookingService.cancelBooking(booking.getId(), request, member.getId());

		// then
		Booking canceledBooking = bookingRepository.findBookingInfoById(booking.getId()).get();

		assertThat(canceledBooking.getStatus()).isEqualTo(BookingStatus.CANCELED);

		assertThat(canceledBooking.getCancel().getReason()).isEqualTo(cancelReason);
		assertThat(canceledBooking.getCancel().getAmount()).isEqualTo(booking.getAmount());

		assertThat(canceledBooking.getPayment().getStatus()).isEqualTo(PaymentStatus.CANCELED);

		assertThat(canceledBooking.getTickets())
			.extracting("seat")
			.extracting("status")
			.containsOnly(EventSeatStatus.AVAILABLE);
	}

	@Test
	void 취소가_가능한_상태가_아니라면_예매를_취소할_수_없다() {
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

		Booking booking = BookingFactory.generate(
			member,
			time,
			seat.getEventSeatArea().getPrice(),
			BookingStatus.CANCELED
		);
		Ticket ticket = TicketFactory.generate(seat);
		booking.addTicket(ticket);
		Payment payment = PaymentFactory.generate(PaymentMethod.CARD, booking.getAmount(), PaymentStatus.CANCELED);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		String cancelReason = "단순 변심";

		BookingCancelRequest request = new BookingCancelRequest(
			cancelReason,
			Optional.empty()
		);

		// when & then
		assertThatThrownBy(() -> bookingService.cancelBooking(booking.getId(), request, member.getId()))
			.isInstanceOf(BookingException.class)
			.hasMessage(BookingErrorCode.UNCANCELABLE_BOOKING.getMessage());
	}

	@Test
	void 결제_수단이_가상게좌인데_환불계좌정보가_없다면_예매를_취소할_수_없다() {
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

		Booking booking = BookingFactory.generate(
			member,
			time,
			seat.getEventSeatArea().getPrice(),
			BookingStatus.PAYMENT_COMPLETED
		);
		Ticket ticket = TicketFactory.generate(seat);
		booking.addTicket(ticket);
		Payment payment = PaymentFactory.generate(PaymentMethod.VIRTUAL_ACCOUNT, booking.getAmount(),
			PaymentStatus.DONE);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		String cancelReason = "단순 변심";

		BookingCancelRequest request = new BookingCancelRequest(
			cancelReason,
			Optional.empty()
		);

		// when & then
		assertThatThrownBy(() -> bookingService.cancelBooking(booking.getId(), request, member.getId()))
			.isInstanceOf(BookingException.class)
			.hasMessage(BookingErrorCode.REFUND_ACCOUNT_REQUIRED.getMessage());
	}

	@Test
	void 결제_수단_상관없이_결제완료_상태이면_환불금액은_amount이다() {
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

		Booking booking = BookingFactory.generate(
			member,
			time,
			seat.getEventSeatArea().getPrice(),
			BookingStatus.PAYMENT_COMPLETED
		);
		Ticket ticket = TicketFactory.generate(seat);
		booking.addTicket(ticket);
		Payment payment = PaymentFactory.generate(PaymentMethod.CARD, booking.getAmount(), PaymentStatus.DONE);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		String cancelReason = "단순 변심";

		BookingCancelRequest request = new BookingCancelRequest(
			cancelReason,
			Optional.empty()
		);

		// when
		bookingService.cancelBooking(booking.getId(), request, member.getId());

		// then
		Booking canceledBooking = bookingRepository.findBookingInfoById(booking.getId()).get();
		assertThat(canceledBooking.getCancel().getAmount()).isEqualTo(booking.getAmount());
	}

	@Test
	void 결제_수단이_가상계좌이고_결제대기_상태이면_환불금액은_0이다() {
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

		Booking booking = BookingFactory.generate(
			member,
			time,
			seat.getEventSeatArea().getPrice(),
			BookingStatus.WAITING_FOR_PAYMENT
		);
		Ticket ticket = TicketFactory.generate(seat);
		booking.addTicket(ticket);
		Payment payment = PaymentFactory.generate(PaymentMethod.VIRTUAL_ACCOUNT, booking.getAmount(),
			PaymentStatus.WAITING_FOR_DEPOSIT);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		String cancelReason = "단순 변심";

		BookingCancelRequest request = new BookingCancelRequest(
			cancelReason,
			Optional.of(
				new RefundAccountRequest(
					BankCode.신한.getBankNumCode(),
					"계좌번호",
					"예금주"
				)
			)
		);

		// when
		bookingService.cancelBooking(booking.getId(), request, member.getId());

		// then
		Booking canceledBooking = bookingRepository.findBookingInfoById(booking.getId()).get();
		assertThat(canceledBooking.getCancel().getAmount()).isEqualTo(0);
	}
}
