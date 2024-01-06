// package com.pgms.apibooking.service;
//
// import static org.assertj.core.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.time.LocalDateTime;
// import java.util.List;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.web.client.RestTemplate;
//
// import com.pgms.apibooking.payment.dto.request.PaymentConfirmRequest;
// import com.pgms.apibooking.payment.dto.response.PaymentCardResponse;
// import com.pgms.apibooking.payment.dto.response.PaymentSuccessResponse;
// import com.pgms.coredomain.domain.booking.Booking;
// import com.pgms.coredomain.domain.booking.BookingStatus;
// import com.pgms.coredomain.domain.booking.Payment;
// import com.pgms.coredomain.domain.booking.PaymentMethod;
// import com.pgms.coredomain.domain.booking.PaymentStatus;
// import com.pgms.coredomain.domain.booking.ReceiptType;
// import com.pgms.coredomain.domain.booking.repository.BookingRepository;
// import com.pgms.coredomain.domain.booking.repository.PaymentRepository;
// import com.pgms.coredomain.domain.event.Event;
// import com.pgms.coredomain.domain.event.EventHall;
// import com.pgms.coredomain.domain.event.EventHallSeat;
// import com.pgms.coredomain.domain.event.EventSeat;
// import com.pgms.coredomain.domain.event.EventSeatArea;
// import com.pgms.coredomain.domain.event.EventSeatStatus;
// import com.pgms.coredomain.domain.event.EventTime;
// import com.pgms.coredomain.domain.event.GenreType;
// import com.pgms.coredomain.domain.event.SeatAreaType;
// import com.pgms.coredomain.domain.event.repository.EventHallRepository;
// import com.pgms.coredomain.domain.event.repository.EventRepository;
// import com.pgms.coredomain.domain.event.repository.EventSeatAreaRepository;
// import com.pgms.coredomain.domain.event.repository.EventSeatRepository;
// import com.pgms.coredomain.domain.event.repository.EventTimeRepository;
//
// @SpringBootTest
// @ExtendWith(MockitoExtension.class)
// class PaymentServiceTest {
//
//
// 	@Autowired
// 	private EventHallRepository eventHallRepository;
//
// 	@Autowired
// 	private EventTimeRepository eventTimeRepository;
//
// 	@Autowired
// 	private EventRepository eventRepository;
//
// 	@Autowired
// 	private EventSeatAreaRepository eventSeatAreaRepository;
//
// 	@Mock
// 	private RestTemplate restTemplate;
//
// 	private Booking booking;
//
//
// 	@Autowired
// 	private BookingRepository bookingRepository;
//
// 	@Autowired
// 	private PaymentRepository paymentRepository;
// 	@MockBean
// 	public TossPaymentService tossPaymentService;
// 	@Autowired
// 	private EventSeatRepository eventSeatRepository;
//
// 	@BeforeEach
// 	public void setUp() {
// 		EventHallSeat eventHallSeat = new EventHallSeat("A1");
// 		EventHall eventHall = eventHallRepository.save(EventHall.builder()
// 			.name("예술공간서울")
// 			.address("서울특별시 종로구 명륜2가 성균관로4길 19")
// 			.eventHallSeats(List.of(eventHallSeat))
// 			.build());
//
// 		Event event = eventRepository.save(Event.builder()
// 			.title("공연 1")
// 			.description("공연1 입니다.")
// 			.runningTime(60)
// 			.startedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
// 			.endedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
// 			.viewRating("12세 이상")
// 			.genreType(GenreType.MUSICAL)
// 			.thumbnail("thumbnail.jpg")
// 			.bookingStartedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
// 			.bookingEndedAt(LocalDateTime.of(2023, 1, 1, 0, 0))
// 			.eventHall(eventHall)
// 			.build());
//
// 		EventTime time = eventTimeRepository.save(new EventTime(
// 			1,
// 			LocalDateTime.of(2024, 1, 1, 0, 0),
// 			LocalDateTime.of(2024, 1, 1, 0, 0),
// 			event));
//
// 		EventSeatArea eventSeatArea = eventSeatAreaRepository.save(new EventSeatArea(
// 			SeatAreaType.S,
// 			100000,
// 			event));
//
// 		eventSeatRepository.save(
// 			EventSeat.builder()
// 			.name("A1")
// 			.status(EventSeatStatus.AVAILABLE)
// 			.eventSeatArea(eventSeatArea)
// 			.eventTime(time).build());
//
// 		booking = Booking.builder()
// 			.id("bookingTestId")
// 			.time(time)
// 			.bookingName("BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL 1")
// 			.status(BookingStatus.WAITING_FOR_PAYMENT)
// 			.receiptType(ReceiptType.DELIVERY)
// 			.buyerName("김토스")
// 			.buyerPhoneNumber("010-123-456")
// 			.recipientName("Jane Doe")
// 			.recipientPhoneNumber("9876543210")
// 			.streetAddress("456 Oak St")
// 			.detailAddress("Apt 301")
// 			.zipCode("54321")
// 			.amount(180000)
// 			.build();
// 		bookingRepository.save(booking);
// 	}
//
// 	@Test
// 	public void 카드_결제_성공_테스트() {
// 		// given
// 		String paymentKey = "paymentKey";
// 		Payment payment = Payment.builder()
// 			.method(PaymentMethod.CARD)
// 			.amount(booking.getAmount())
// 			.status(PaymentStatus.READY)
// 			.build();
// 		booking.updatePayment(payment);
// 		paymentRepository.save(payment);
//
// 		PaymentSuccessResponse mockSuccessResponse = new PaymentSuccessResponse(
// 			paymentKey,
// 			booking.getId(),
// 			booking.getBookingName(),
// 			payment.getMethod().getDescription(),
// 			payment.getAmount(),
// 			"DONE",
// 			"2024-01-01T10:01:00+05:00",
// 			"2024-01-01T10:01:00+05:00",
// 			new PaymentCardResponse("card_num", 2, false),
// 			null
// 		);
//
// 		PaymentService paymentService = new PaymentService(paymentRepository, bookingRepository, tossPaymentService);
//
// 		when(tossPaymentService.requestTossPaymentConfirmation(any(PaymentConfirmRequest.class)))
// 			.thenReturn(mockSuccessResponse);
//
// 		// when
// 		PaymentSuccessResponse response = paymentService.successPayment(paymentKey, booking.getId(),
// 			booking.getAmount());
//
//
// 		// then
// 		verify(tossPaymentService, times(1)).requestTossPaymentConfirmation(any(PaymentConfirmRequest.class));
//
// 		assertThat(response.status()).isEqualTo("DONE");
// 		assertThat(payment.getCardNumber()).isEqualTo(response.card().number());
//
// 		assertThat(booking.getStatus()).isEqualTo(BookingStatus.PAYMENT_COMPLETED);
// 		assertThat(payment.getAccountNumber()).isNull();
// 	}
//
// 	// @Test
// 	// public void 결제_가격_불일치_오류_테스트() {
// 	// 	// given
// 	// 	String paymentKey = "paymentKey";
// 	// 	Payment payment = Payment.builder()
// 	// 		.method(PaymentMethod.CARD)
// 	// 		.amount(booking.getAmount())
// 	// 		.status(PaymentStatus.READY)
// 	// 		.build();
// 	// 	booking.updatePayment(payment);
// 	// 	when(bookingRepository.findWithPaymentById(anyString())).thenReturn(Optional.of(booking));
// 	//
// 	// 	// when & then
// 	// 	assertThatThrownBy(() -> paymentService.successPayment(paymentKey, booking.getId(), 170000))
// 	// 		.isInstanceOf(BookingException.class)
// 	// 		.hasMessageContaining(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH.getMessage());
// 	// }
// 	//
// 	// @Test
// 	// public void 카드_결제_취소_테스트() {
// 	// 	String paymentKey = "paymentKey";
// 	// 	Payment payment = Payment.builder()
// 	// 		.method(PaymentMethod.CARD)
// 	// 		.amount(booking.getAmount())
// 	// 		.status(PaymentStatus.READY)
// 	// 		.build();
// 	// 	booking.updatePayment(payment);
// 	//
// 	// 	BookingCancelRequest cancelRequest = new BookingCancelRequest(
// 	// 		"고객이 결제 취소",
// 	// 		180000,
// 	// 		Optional.of(new RefundAccountRequest("20", "352-123", "김환불"))
// 	// 	);
// 	//
// 	// 	PaymentCancelResponse mockCancelResponse = new PaymentCancelResponse(
// 	// 		paymentKey,
// 	// 		booking.getId(),
// 	// 		booking.getBookingName(),
// 	// 		payment.getMethod().getDescription(),
// 	// 		"180000",
// 	// 		"CANCELED",
// 	// 		"2024-01-01T10:01:00+05:00",
// 	// 		"2024-01-01T10:01:00+05:00",
// 	// 		new PaymentCardResponse("card_num", 2, false),
// 	// 		null,
// 	// 		List.of(new PaymentCancelDetailResponse(cancelRequest.cancelReason(), 180000, "2024-01-02T10:01:00+05:00"))
// 	// 	);
// 	//
// 	// 	when(paymentRepository.findByPaymentKey(paymentKey)).thenReturn(Optional.of(payment));
// 	// 	when(restTemplate.postForObject(any(), any(HttpEntity.class), eq(PaymentCancelResponse.class)))
// 	// 		.thenReturn(mockCancelResponse);
// 	//
// 	// 	// when
// 	// 	PaymentCancelResponse response = paymentService.cancelPayment(paymentKey, cancelRequest);
// 	//
// 	// 	// then
// 	// 	verify(restTemplate, times(1)).postForObject(any(), any(HttpEntity.class), eq(PaymentCancelResponse.class));
// 	// 	assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
// 	// 	assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
// 	// }
// }
