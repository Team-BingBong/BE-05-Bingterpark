// package com.pgms.apibooking.service;
//
// import static org.assertj.core.api.Assertions.assertThat;
// import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
// import static org.mockito.Mockito.*;
//
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpEntity;
// import org.springframework.web.client.RestTemplate;
//
// import com.pgms.apibooking.dto.request.BookingCancelRequest;
// import com.pgms.apibooking.dto.request.RefundAccountRequest;
// import com.pgms.apibooking.dto.response.PaymentCancelDetailResponse;
// import com.pgms.apibooking.dto.response.PaymentCancelResponse;
// import com.pgms.apibooking.dto.response.PaymentCardResponse;
// import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
// import com.pgms.apibooking.exception.BookingErrorCode;
// import com.pgms.apibooking.exception.BookingException;
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
// import com.pgms.coredomain.domain.event.EventTime;
// import com.pgms.coredomain.domain.event.GenreType;
// import com.pgms.coredomain.domain.event.repository.EventHallRepository;
// import com.pgms.coredomain.domain.event.repository.EventRepository;
// import com.pgms.coredomain.domain.event.repository.EventTimeRepository;
//
// @ExtendWith(MockitoExtension.class)
// @SpringBootTest
// class PaymentServiceTest {
//
// 	@Autowired
// 	private BookingRepository bookingRepository;
//
// 	@Autowired
// 	private PaymentRepository paymentRepository;
//
// 	@Autowired
// 	private PaymentService paymentService;
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
// 	@Mock
// 	private RestTemplate restTemplate;
//
// 	private Booking booking;
// 	private EventHall eventHall;
// 	private EventTime time;
// 	private Event event;
//
// 	@BeforeEach
// 	public void setUp() {
// 		eventHall = eventHallRepository.save(EventHall.builder()
// 			.name("예술공간서울")
// 			.address("서울특별시 종로구 명륜2가 성균관로4길 19")
// 			.eventHallSeats(new ArrayList<>())
// 			.build());
//
// 		event = eventRepository.save(Event.builder()
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
// 		time = eventTimeRepository.save(new EventTime(
// 			1,
// 			LocalDateTime.of(2024, 1, 1, 0, 0),
// 			LocalDateTime.of(2024, 1, 1, 0, 0),
// 			event));
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
// 		when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(PaymentSuccessResponse.class)))
// 			.thenReturn(mockSuccessResponse);
//
// 		// when
// 		PaymentSuccessResponse response = paymentService.successPayment(paymentKey, booking.getId(),
// 			booking.getAmount());
//
// 		// then
// 		verify(restTemplate, times(1)).postForObject(anyString(), any(HttpEntity.class),
// 			eq(PaymentSuccessResponse.class));
//
// 		assertThat(booking.getStatus()).isEqualTo(BookingStatus.PAYMENT_COMPLETED);
// 		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.DONE);
// 		assertThat(payment.getCardNumber()).isEqualTo(response.card().number());
// 		assertThat(payment.getAccountNumber()).isNull();
// 	}
//
// 	@Test
// 	public void 결제_가격_불일치_오류_테스트() {
// 		// given
// 		String paymentKey = "paymentKey";
// 		Payment payment = Payment.builder()
// 			.method(PaymentMethod.CARD)
// 			.amount(booking.getAmount())
// 			.status(PaymentStatus.READY)
// 			.build();
// 		booking.updatePayment(payment);
// 		when(bookingRepository.findWithPaymentById(anyString())).thenReturn(Optional.of(booking));
//
// 		// when & then
// 		assertThatThrownBy(() -> paymentService.successPayment(paymentKey, booking.getId(), 170000))
// 			.isInstanceOf(BookingException.class)
// 			.hasMessageContaining(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH.getMessage());
// 	}
//
// 	@Test
// 	public void 카드_결제_취소_테스트() {
// 		String paymentKey = "paymentKey";
// 		Payment payment = Payment.builder()
// 			.method(PaymentMethod.CARD)
// 			.amount(booking.getAmount())
// 			.status(PaymentStatus.READY)
// 			.build();
// 		booking.updatePayment(payment);
//
// 		BookingCancelRequest cancelRequest = new BookingCancelRequest(
// 			"고객이 결제 취소",
// 			180000,
// 			Optional.of(new RefundAccountRequest("20", "352-123", "김환불"))
// 		);
//
// 		PaymentCancelResponse mockCancelResponse = new PaymentCancelResponse(
// 			paymentKey,
// 			booking.getId(),
// 			booking.getBookingName(),
// 			payment.getMethod().getDescription(),
// 			"180000",
// 			"CANCELED",
// 			"2024-01-01T10:01:00+05:00",
// 			"2024-01-01T10:01:00+05:00",
// 			new PaymentCardResponse("card_num", 2, false),
// 			null,
// 			List.of(new PaymentCancelDetailResponse(cancelRequest.cancelReason(), 180000, "2024-01-02T10:01:00+05:00"))
// 		);
//
// 		when(paymentRepository.findByPaymentKey(paymentKey)).thenReturn(Optional.of(payment));
// 		when(restTemplate.postForObject(any(), any(HttpEntity.class), eq(PaymentCancelResponse.class)))
// 			.thenReturn(mockCancelResponse);
//
// 		// when
// 		PaymentCancelResponse response = paymentService.cancelPayment(paymentKey, cancelRequest);
//
// 		// then
// 		verify(restTemplate, times(1)).postForObject(any(), any(HttpEntity.class), eq(PaymentCancelResponse.class));
// 		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
// 		assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
// 	}
// }
