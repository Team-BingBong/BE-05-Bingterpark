package com.pgms.apibooking.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.NoSuchElementException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.pgms.apibooking.config.TossPaymentConfig;
import com.pgms.apibooking.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.dto.request.PaymentCreateRequest;
import com.pgms.apibooking.dto.response.PaymentCreateResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
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
			.orElseThrow(() -> new NoSuchElementException("Booking not found"));
		Payment payment = paymentRepository.save(request.toEntity(booking));
		return PaymentCreateResponse.of(payment, tossPaymentConfig.getSuccessUrl(), tossPaymentConfig.getFailUrl());
	}

	public PaymentSuccessResponse succeedPayment(String paymentKey, String bookingId, int amount) {
		Payment payment = getAndVerifyPayment(bookingId, amount);
		PaymentSuccessResponse response = requestPaymentConfirmation(paymentKey, bookingId, amount);

		switch (payment.getMethod()) {
			case CARD -> payment.updateCardInfo(response.card().number(), response.card().installmentPlanMonths(),
				response.card().isInterestFree());
			case VIRTUAL_ACCOUNT -> System.out.println("아직 안함");
			default -> throw new IllegalArgumentException("결제 수단이 올바르지 않습니다.");
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
		payment.updateConfirmInfo(paymentKey, LocalDateTime.parse(response.approvedAt(), formatter),
			LocalDateTime.parse(response.requestedAt(), formatter));
		return response;
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
			throw new RuntimeException("Toss Payments API 오류", e);
		} catch (HttpServerErrorException e) {
			throw new RuntimeException("Toss Payments API 서버 오류", e);
		} catch (Exception e) {
			throw new RuntimeException("Toss Payments 결제 승인 요청 오류", e);
		}
	}

	private Payment getAndVerifyPayment(String bookingId, int amount) {
		// TODO: bookingId String으로 변경시 수정 필요.
		Payment payment = paymentRepository.findByBookingId(1L).orElseThrow(() -> {
			throw new NoSuchElementException("해당 주문에 대한 payment 정보가 없습니다.");
		});
		if (payment.getAmount() != amount) {
			throw new IllegalArgumentException("결제 가격 정보가 일치하지 않습니다.");
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
}
