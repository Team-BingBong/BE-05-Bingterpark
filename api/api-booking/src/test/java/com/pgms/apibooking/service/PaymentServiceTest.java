package com.pgms.apibooking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.dto.request.RefundAccountRequest;
import com.pgms.apibooking.dto.response.PaymentCancelDetailResponse;
import com.pgms.apibooking.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.dto.response.PaymentCardResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.PaymentRepository;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private PaymentRepository paymentRepository;

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private TossPaymentConfig tossPaymentConfig;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private PaymentService paymentService;

	private Booking booking;

	@BeforeEach
	public void setUp() {
		booking = Booking.builder()
			.id("bookingTestId")
			.bookingName("BLACKPINK WORLD TOUR ［BORN PINK］ FINALE IN SEOUL 1")
			.status(BookingStatus.WAITING_FOR_PAYMENT)
			.receiptType(ReceiptType.DELIVERY)
			.buyerName("김토스")
			.buyerPhoneNumber("010-123-456")
			.recipientName("Jane Doe")
			.recipientPhoneNumber("9876543210")
			.streetAddress("456 Oak St")
			.detailAddress("Apt 301")
			.zipCode("54321")
			.amount(180000)
			.build();
	}

	@Test
	public void 카드_결제_성공_테스트() {
		// given
		String paymentKey = "paymentKey";
		Payment payment = Payment.builder()
			.method(PaymentMethod.CARD)
			.amount(booking.getAmount())
			.status(PaymentStatus.READY)
			.build();
		booking.updatePayment(payment);
		PaymentSuccessResponse mockSuccessResponse = new PaymentSuccessResponse(
			paymentKey,
			booking.getId(),
			booking.getBookingName(),
			payment.getMethod().getDescription(),
			payment.getAmount(),
			"DONE",
			"2024-01-01T10:01:00+05:00",
			"2024-01-01T10:01:00+05:00",
			new PaymentCardResponse("card_num", 2, false),
			null
		);

		when(bookingRepository.findWithPaymentById(anyString())).thenReturn(Optional.of(booking));
		when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(PaymentSuccessResponse.class)))
			.thenReturn(mockSuccessResponse);

		// when
		PaymentSuccessResponse response = paymentService.successPayment(paymentKey, booking.getId(), 180000);

		// then
		verify(bookingRepository, times(1)).findWithPaymentById(anyString());
		verify(restTemplate, times(1)).postForObject(anyString(), any(HttpEntity.class),
			eq(PaymentSuccessResponse.class));

		assertThat(booking.getStatus()).isEqualTo(BookingStatus.PAYMENT_COMPLETED);
		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.DONE);
		assertThat(payment.getCardNumber()).isEqualTo(response.card().number());
		assertThat(payment.getAccountNumber()).isNull();
	}

	@Test
	public void 결제_가격_불일치_오류_테스트() {
		// given
		String paymentKey = "paymentKey";
		Payment payment = Payment.builder()
			.method(PaymentMethod.CARD)
			.amount(booking.getAmount())
			.status(PaymentStatus.READY)
			.build();
		booking.updatePayment(payment);
		when(bookingRepository.findWithPaymentById(anyString())).thenReturn(Optional.of(booking));

		// when & then
		assertThatThrownBy(() -> paymentService.successPayment(paymentKey, booking.getId(), 170000))
			.isInstanceOf(BookingException.class)
			.hasMessageContaining(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH.getMessage());
	}

	@Test
	public void 카드_결제_취소_테스트() {
		String paymentKey = "paymentKey";
		Payment payment = Payment.builder()
			.method(PaymentMethod.CARD)
			.amount(booking.getAmount())
			.status(PaymentStatus.READY)
			.build();
		booking.updatePayment(payment);

		BookingCancelRequest cancelRequest = new BookingCancelRequest(
			"고객이 결제 취소",
			180000,
			Optional.of(new RefundAccountRequest("20", "352-123", "김환불"))
		);

		PaymentCancelResponse mockCancelResponse = new PaymentCancelResponse(
			paymentKey,
			booking.getId(),
			booking.getBookingName(),
			payment.getMethod().getDescription(),
			"180000",
			"CANCELED",
			"2024-01-01T10:01:00+05:00",
			"2024-01-01T10:01:00+05:00",
			new PaymentCardResponse("card_num", 2, false),
			null,
			List.of(new PaymentCancelDetailResponse(cancelRequest.cancelReason(), 180000, "2024-01-02T10:01:00+05:00"))
		);

		when(paymentRepository.findByPaymentKey(paymentKey)).thenReturn(Optional.of(payment));
		when(restTemplate.postForObject(any(), any(HttpEntity.class), eq(PaymentCancelResponse.class)))
			.thenReturn(mockCancelResponse);

		// when
		PaymentCancelResponse response = paymentService.cancelPayment(paymentKey, cancelRequest);

		// then
		verify(restTemplate, times(1)).postForObject(any(), any(HttpEntity.class), eq(PaymentCancelResponse.class));
		assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELED);
		assertThat(booking.getStatus()).isEqualTo(BookingStatus.CANCELED);
	}
}
