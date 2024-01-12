package com.pgms.apibooking.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.config.TestConfig;
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
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.Ticket;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.PaymentRepository;
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
	private Booking booking;
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
	public void 예매_카드_결제를_진행한다() {
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

		//when
		PaymentSuccessResponse response = paymentService.successPayment(PAYMENT_KEY, booking.getId(),
			payment.getAmount());

		//then
		payment = paymentRepository.findByPaymentKey(PAYMENT_KEY).get();
		booking = bookingRepository.findWithPaymentById(booking.getId()).get();

		assertThat(response.status()).isEqualTo(PaymentStatus.DONE.toString());
		assertThat(response.card().number()).isEqualTo(payment.getCardNumber());

		assertThat(booking.getStatus()).isEqualTo(BookingStatus.PAYMENT_COMPLETED);
		assertThat(payment.getAccountNumber()).isNull();
	}

	// @Test
	// public void 예매가격_결제가격_불일치시_결제를_진행할_수_없다() {
	// 	// given
	// 	String paymentKey = "paymentKey";
	// 	Payment payment = Payment.builder()
	// 		.method(PaymentMethod.CARD)
	// 		.amount(booking.getAmount())
	// 		.status(PaymentStatus.READY)
	// 		.build();
	// 	booking.updatePayment(payment);
	// 	when(bookingRepository.findWithPaymentById(anyString())).thenReturn(Optional.of(booking));
	//
	// 	// when & then
	// 	assertThatThrownBy(() -> paymentService.successPayment(paymentKey, booking.getId(), 170000))
	// 		.isInstanceOf(BookingException.class)
	// 		.hasMessageContaining(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH.getMessage());
	// }
	//
	// @Test
	// public void 카드_결제_취소_테스트() {
	// 	String paymentKey = "paymentKey";
	// 	Payment payment = Payment.builder()
	// 		.method(PaymentMethod.CARD)
	// 		.amount(booking.getAmount())
	// 		.status(PaymentStatus.READY)
	// 		.build();
	// 	booking.updatePayment(payment);
	//
	// 	BookingCancelRequest cancelRequest = new BookingCancelRequest(
	// 		"고객이 결제 취소",
	// 		180000,
	// 		Optional.of(new RefundAccountRequest("20", "352-123", "김환불"))
	// 	);
	//
	// 	PaymentCancelResponse mockCancelResponse = new PaymentCancelResponse(
	// 		paymentKey,
	// 		booking.getId(),
	// 		booking.getBookingName(),
	// 		payment.getMethod().getDescription(),
	// 		"180000",
	// 		"CANCELED",
	// 		"2024-01-01T10:01:00+05:00",
	// 		"2024-01-01T10:01:00+05:00",
	// 		new PaymentCardResponse("card_num", 2, false),
	// 		null,
	// 		List.of(new PaymentCancelDetailResponse(cancelRequest.cancelReason(), 180000, "2024-01-02T10:01:00+05:00"))
	// 	);
	//
	// 	when(paymentRepository.findByPaymentKey(paymentKey)).thenReturn(Optional.of(payment));
	// 	when(restTemplate.postForObject(any(), any(HttpEntity.class), eq(PaymentCancelResponse.class)))
	// 		.thenReturn(mockCancelResponse);
	//
	// 	// when
	// 	PaymentCancelResponse response = paymentService.cancelPayment(paymentKey, cancelRequest);
	//
	// 	// then
	// 	verify(restTemplate, times(1)).postForObject(any(), any(HttpEntity.class), eq(PaymentCancelResponse.class));
	// 	assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
	// 	assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
	// }
}
