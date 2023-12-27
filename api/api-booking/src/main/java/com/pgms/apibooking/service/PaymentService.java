package com.pgms.apibooking.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.dto.request.PaymentCreateRequest;
import com.pgms.apibooking.dto.response.PaymentCreateResponse;
import com.pgms.apibooking.dto.response.PaymentFailResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final BookingRepository bookingRepository;
	private final TossPaymentConfig tossPaymentConfig;

	public PaymentCreateResponse createPayment(PaymentCreateRequest request) {
		// TODO: booking 생성 로직 추가하기
		Booking booking = bookingRepository.findById(request.bookingId())
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));
		Payment payment = paymentRepository.save(request.toEntity(booking));
		return PaymentCreateResponse.of(payment, tossPaymentConfig.getSuccessUrl(), tossPaymentConfig.getFailUrl());
	}

	public PaymentSuccessResponse successPayment(String paymentKey, String bookingId, int amount) {
		Payment payment = getAndVerifyPayment(bookingId, amount);
		PaymentSuccessResponse response = requestPaymentConfirmation(paymentKey, bookingId, amount);

		switch (payment.getMethod()) {
			case CARD -> payment.updateCardInfo(response.card().number(), response.card().installmentPlanMonths(),
				response.card().isInterestFree());
			case VIRTUAL_ACCOUNT -> System.out.println("아직 안함");
			default -> throw new BookingException(BookingErrorCode.INVALID_PAYMENT_METHOD);
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
		payment.updateConfirmInfo(paymentKey, LocalDateTime.parse(response.approvedAt(), formatter),
			LocalDateTime.parse(response.requestedAt(), formatter));
		return response;
	}

	public PaymentFailResponse failPayment(String errorCode, String errorMessage, String orderId) {
		// TODO: orderId String 형식으로 바뀌면 수정 필요
		Payment payment = getPaymentByBookingId(1L);
		payment.toAborted();
		payment.updateFailedMsg(errorMessage);
		return new PaymentFailResponse(errorCode, errorMessage, orderId);
	}

	public PaymentSuccessResponse requestPaymentConfirmation(String paymentKey, String bookingId, int amount) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = buildTossApiHeaders();
		PaymentConfirmRequest request = new PaymentConfirmRequest(paymentKey, bookingId, amount);
		try { // tossPayments post 요청 (url , HTTP 객체 ,응답 Dto)
			ResponseEntity<PaymentSuccessResponse> response = restTemplate.postForEntity(
				TossPaymentConfig.URL, new HttpEntity<>(request, headers), PaymentSuccessResponse.class);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			throw new BookingException(BookingErrorCode.TOSS_PAYMENTS_ERROR);
		} catch (Exception e) {
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private Payment getAndVerifyPayment(String bookingId, int amount) {
		// TODO: bookingId String으로 변경시 수정 필요.
		Payment payment = getPaymentByBookingId(1L);
		if (payment.getAmount() != amount) {
			throw new BookingException(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH);
		}
		return payment;
	}

	private HttpHeaders buildTossApiHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String encodedAuthKey = new String(
			Base64.getEncoder()
				.encode((tossPaymentConfig.getTestSecretApiKey() + ":").getBytes(StandardCharsets.UTF_8)));
		headers.setBasicAuth(encodedAuthKey);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return headers;
	}

	private Payment getPaymentByBookingId(Long bookingId) {
		return paymentRepository.findByBookingId(bookingId).orElseThrow(() -> {
			throw new BookingException(BookingErrorCode.PAYMENT_NOT_FOUND);
		});
	}
}
