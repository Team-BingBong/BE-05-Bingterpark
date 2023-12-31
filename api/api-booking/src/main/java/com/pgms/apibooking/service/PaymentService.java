package com.pgms.apibooking.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
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
import com.pgms.apibooking.dto.request.PaymentCreateRequest;
import com.pgms.apibooking.dto.request.RefundAccountRequest;
import com.pgms.apibooking.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.dto.response.PaymentCardResponse;
import com.pgms.apibooking.dto.response.PaymentCreateResponse;
import com.pgms.apibooking.dto.response.PaymentFailResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.dto.response.PaymentVirtualResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.apibooking.util.DateTimeUtil;
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
	private final BookingService bookingService;

	public PaymentCreateResponse createPayment(PaymentCreateRequest request) {
		Booking booking = bookingService.createBooking(request);
		Payment payment = request.toEntity(booking);
		paymentRepository.save(payment);
		return PaymentCreateResponse.of(payment, tossPaymentConfig.getSuccessUrl(), tossPaymentConfig.getFailUrl());
	}

	public PaymentSuccessResponse successPayment(String paymentKey, String bookingId, int amount) {
		Booking booking = bookingRepository.findWithPaymentById(bookingId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));
		Payment payment = booking.getPayment();

		if (payment.getAmount() != amount) {
			throw new BookingException(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH);
		}

		PaymentSuccessResponse response = requestPaymentConfirmation(paymentKey, bookingId, amount);

		switch (payment.getMethod()) {
			case CARD -> {
				PaymentCardResponse card = response.card();
				payment.updateCardInfo(
					card.number(),
					card.installmentPlanMonths(),
					card.isInterestFree()
				);
				payment.updateCardSuccess(DateTimeUtil.parse(response.approvedAt()));
			}
			case VIRTUAL_ACCOUNT -> {
				PaymentVirtualResponse virtualAccount = response.virtualAccount();
				payment.updateVirtualWaiting(
					virtualAccount.accountNumber(),
					virtualAccount.bankCode(),
					virtualAccount.customerName(),
					DateTimeUtil.parse(virtualAccount.dueDate())
				);
			}
			default -> throw new BookingException(BookingErrorCode.INVALID_PAYMENT_METHOD);
		}
		payment.updateConfirmInfo(paymentKey, DateTimeUtil.parse(response.requestedAt()));
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
			throw new BookingException(BookingErrorCode.TOSS_PAYMENTS_ERROR);
		} catch (Exception e) {
			log.warn(e.getMessage());
			throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public PaymentCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request) {
		Payment payment = getPaymentByPaymentKey(paymentKey);
		PaymentCancelResponse response = requestPaymentCancellation(paymentKey, request);
		if (request.refundReceiveAccount().isPresent()) {
			RefundAccountRequest refundAccountRequest = request.refundReceiveAccount().get();
			payment.updateRefundInfo(
				refundAccountRequest.bank(),
				refundAccountRequest.accountNumber(),
				refundAccountRequest.holderName()
			);
		}
		Booking booking = payment.getBooking();
		payment.toCanceled(DateTimeUtil.parse(response.approvedAt()));
		booking.updateStatus(BookingStatus.CANCELLED);
		// TODO: ticket(event seat) 상태 변경
		return response;
	}

	public PaymentCancelResponse requestPaymentCancellation(String paymentKey, PaymentCancelRequest request) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = buildTossApiHeaders();
		URI uri = URI.create(TossPaymentConfig.TOSS_ORIGIN_URL + paymentKey + "/cancel");
		try {
			return restTemplate.postForObject(
				uri, new HttpEntity<>(request, headers), PaymentCancelResponse.class);
		} catch (Exception e) {
			log.warn(e.getMessage());
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
