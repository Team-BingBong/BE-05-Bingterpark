package com.pgms.apibooking.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.dto.response.PaymentFailResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final BookingRepository bookingRepository;
	private final TossPaymentConfig tossPaymentConfig;

	public PaymentSuccessResponse successPayment(String paymentKey, String bookingId, int amount) {
		Booking booking = bookingRepository.findWithPaymentById(bookingId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));
		Payment payment = booking.getPayment();

		if (payment.getAmount() != amount) {
			throw new BookingException(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH);
		}

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
		booking.updateStatus(BookingStatus.PAYMENT_COMPLETED);

		return response;
	}

	public PaymentFailResponse failPayment(String errorCode, String errorMessage, String bookingId) {
		Payment payment = getPaymentByBookingId(bookingId);
		payment.toAborted();
		payment.updateFailedMsg(errorMessage);
		return new PaymentFailResponse(errorCode, errorMessage, bookingId);
	}

	public PaymentSuccessResponse requestPaymentConfirmation(String paymentKey, String bookingId, int amount) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = buildTossApiHeaders();
		PaymentConfirmRequest request = new PaymentConfirmRequest(paymentKey, bookingId, amount);
		try { // tossPayments post 요청 (url , HTTP 객체 ,응답 Dto)
			return restTemplate.postForObject(
				TossPaymentConfig.TOSS_CONFIRM_URL, new HttpEntity<>(request, headers), PaymentSuccessResponse.class);
		} catch (HttpClientErrorException e) {
			log.warn("HttpClientErrorException: {}", e.getMessage());
			throw new BookingException(BookingErrorCode.TOSS_PAYMENTS_ERROR);
		} catch (Exception e) {
			log.error("Exception: {}", e.getMessage(), e);
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public PaymentCancelResponse cancelPayment(PaymentCancelRequest request) {
		Payment payment = getPaymentByPaymentKey(request.paymentKey());
		PaymentCancelResponse response = requestPaymentCancellation(request);
		payment.toCanceled();
		// TODO: booking, ticket 상태 변경
		return response;
	}

	public PaymentCancelResponse requestPaymentCancellation(PaymentCancelRequest request) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = buildTossApiHeaders();
		URI uri = URI.create(TossPaymentConfig.TOSS_ORIGIN_URL + request.paymentKey() + "/cancel");
		try {
			return restTemplate.postForObject(
				uri, new HttpEntity<>(request, headers), PaymentCancelResponse.class);
		} catch (Exception e) {
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private Payment getPaymentByPaymentKey(String paymentKey) {
		return paymentRepository.findByPaymentKey(paymentKey)
			.orElseThrow(() -> new BookingException(BookingErrorCode.PAYMENT_NOT_FOUND));
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

	private Payment getPaymentByBookingId(String bookingId) {
		return paymentRepository.findByBookingId(bookingId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.PAYMENT_NOT_FOUND));
	}
}
