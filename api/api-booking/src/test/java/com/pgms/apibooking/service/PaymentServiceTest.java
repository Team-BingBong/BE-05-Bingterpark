package com.pgms.apibooking.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.config.TestConfig;
import com.pgms.apibooking.domain.payment.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.domain.payment.dto.request.RefundAccountRequest;
import com.pgms.apibooking.domain.payment.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.domain.payment.service.PaymentService;
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
import com.pgms.coredomain.domain.booking.CardIssuer;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.Ticket;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.PaymentRepository;
import com.pgms.coredomain.domain.common.BookingErrorCode;
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
class PaymentServiceTest {

	private static final LocalDateTime NOW = LocalDateTime.now();

	@Autowired
	private EventHallRepository eventHallRepository;

	@Autowired
	private EventTimeRepository eventTimeRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private EventSeatAreaRepository eventSeatAreaRepository;

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private EventSeatRepository eventSeatRepository;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PaymentService paymentService;

	private Member member;
	private static final String PAYMENT_KEY = "paymentTestKey";

	@BeforeEach
	public void setUp() {
		member = memberRepository.save(Member.builder()
			.email("test@gmail.com")
			.password("test1234!")
			.name("홍길동")
			.provider(Provider.KAKAO)
			.phoneNumber("010-123-456")
			.build());
	}

	@Test
	public void 카드_결제를_진행한다() {
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
		Payment payment = PaymentFactory.generate(
			PaymentMethod.CARD,
			booking.getAmount(),
			PaymentStatus.READY
		);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		// when
		PaymentSuccessResponse response = paymentService.successPayment(PAYMENT_KEY, booking.getId(),
			booking.getAmount());

		// then
		payment = paymentRepository.findByPaymentKey(PAYMENT_KEY).get();
		booking = bookingRepository.findWithPaymentById(booking.getId()).get();

		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.DONE);
		assertThat(booking.getStatus()).isEqualTo(BookingStatus.PAYMENT_COMPLETED);

		assertThat(response.paymentKey()).isEqualTo(payment.getPaymentKey());
		assertThat(response.card().number()).isEqualTo(payment.getCardNumber());
		assertThat(CardIssuer.fromOfficialCode(response.card().issuerCode())).isEqualTo(payment.getCardIssuer());
		assertThat(payment.getAccountNumber()).isNull();
	}

	@Test
	public void 예매가격_결제가격_불일치시_결제를_진행할_수_없다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW;
		LocalDateTime paymentRequestedAt = NOW.plusMinutes(2);
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

		int actualAmount = booking.getAmount();
		int notSameAmount = 1000;

		Payment payment = PaymentFactory.generate(
			PaymentMethod.CARD,
			notSameAmount,
			PaymentStatus.READY
		);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		// when & then
		assertThatThrownBy(() -> paymentService.successPayment(PAYMENT_KEY, booking.getId(), actualAmount))
			.isInstanceOf(BookingException.class)
			.hasMessageContaining(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH.getMessage());
	}

	@Test
	public void 결제_수단이_카드_결제일때_결제를_취소한다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW;
		LocalDateTime bookingEndedAt = NOW.plusDays(1);
		LocalDateTime paymentRequestedAt = NOW.plusMinutes(2);

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

		Payment payment = PaymentFactory.generate(
			PaymentMethod.CARD,
			booking.getAmount(),
			PaymentStatus.DONE
		);
		payment.updateConfirmInfo(PAYMENT_KEY, paymentRequestedAt);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		// when
		paymentService.cancelPayment(PAYMENT_KEY, new PaymentCancelRequest(
			"단순 변심",
			payment.getAmount(),
			Optional.empty()
		));

		// then
		payment = paymentRepository.findByPaymentKey(PAYMENT_KEY).get();
		booking = payment.getBooking();

		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
		assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
	}

	@Test
	public void 가상계좌_결제를_진행한다() {
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
		Payment payment = PaymentFactory.generate(
			PaymentMethod.VIRTUAL_ACCOUNT,
			booking.getAmount(),
			PaymentStatus.READY
		);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		// when
		PaymentSuccessResponse response = paymentService.successPayment(PAYMENT_KEY, booking.getId(),
			booking.getAmount());

		// then
		payment = paymentRepository.findByPaymentKey(PAYMENT_KEY).get();
		booking = bookingRepository.findWithPaymentById(booking.getId()).get();

		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.DONE);
		assertThat(booking.getStatus()).isEqualTo(BookingStatus.WAITING_FOR_PAYMENT);

		assertThat(payment.getAccountNumber()).isEqualTo(response.virtualAccount().accountNumber());
		assertThat(payment.getBankCode()).isEqualTo(BankCode.getByBankNumCode(response.virtualAccount().bankCode()));
		assertThat(payment.getDepositorName()).isEqualTo(response.virtualAccount().customerName());
	}

	@Test
	public void 결제_수단이_가상계좌이고_입금전일때_결제를_취소한다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW;
		LocalDateTime bookingEndedAt = NOW.plusDays(1);
		LocalDateTime paymentRequestedAt = NOW.plusMinutes(2);

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

		Payment payment = PaymentFactory.generate(
			PaymentMethod.VIRTUAL_ACCOUNT,
			booking.getAmount(),
			PaymentStatus.WAITING_FOR_DEPOSIT
		);
		payment.updateConfirmInfo(PAYMENT_KEY, paymentRequestedAt);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		// when
		paymentService.cancelPayment(PAYMENT_KEY, new PaymentCancelRequest(
			"단순 변심",
			null,
			Optional.empty()
		));

		// then
		payment = paymentRepository.findByPaymentKey(PAYMENT_KEY).get();
		booking = payment.getBooking();

		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
		assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
	}

	@Test
	public void 결제_수단이_가상계좌이고_입금되었을때_결제를_취소한다() {
		// given
		LocalDateTime eventStartedAt = NOW.plusDays(2);
		LocalDateTime eventEndedAt = NOW.plusDays(2).plusMinutes(120);
		LocalDateTime bookingStartedAt = NOW;
		LocalDateTime bookingEndedAt = NOW.plusDays(1);
		LocalDateTime paymentRequestedAt = NOW.plusMinutes(2);

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

		Payment payment = PaymentFactory.generate(
			PaymentMethod.VIRTUAL_ACCOUNT,
			booking.getAmount(),
			PaymentStatus.DONE
		);
		payment.updateConfirmInfo(PAYMENT_KEY, paymentRequestedAt);
		booking.updatePayment(payment);
		bookingRepository.save(booking);

		// when
		paymentService.cancelPayment(PAYMENT_KEY, new PaymentCancelRequest(
			"단순 변심",
			payment.getAmount(),
			Optional.of(new RefundAccountRequest("20", "X650583171835", "박토스"))
		));

		// then
		payment = paymentRepository.findByPaymentKey(PAYMENT_KEY).get();
		booking = payment.getBooking();

		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
		assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
	}
}
