package com.pgms.apibooking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.dto.request.RefundAccountRequest;
import com.pgms.apibooking.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.dto.response.PaymentCardResponse;
import com.pgms.apibooking.dto.response.PaymentFailResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.dto.response.PaymentVirtualResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.apibooking.util.DateTimeUtil;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentStatus;
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
	private final TossPaymentService tossPaymentService;

	public PaymentSuccessResponse successPayment(String paymentKey, String bookingId, int amount) {
		Booking booking = bookingRepository.findWithPaymentById(bookingId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));
		Payment payment = booking.getPayment();

		if (payment.getAmount() != amount) {
			throw new BookingException(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH);
		}
		PaymentConfirmRequest request = new PaymentConfirmRequest(paymentKey, bookingId, amount);
		PaymentSuccessResponse response = tossPaymentService.requestTossPaymentConfirmation(request);

		switch (payment.getMethod()) {
			case CARD -> {
				PaymentCardResponse card = response.card();
				payment.updateCardInfo(
					card.number(),
					card.installmentPlanMonths(),
					card.isInterestFree()
				);
				payment.updateApprovedAt(DateTimeUtil.parse(response.approvedAt()));
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
		payment.updateStatus(PaymentStatus.valueOf(response.status()));
		booking.updateStatus(BookingStatus.PAYMENT_COMPLETED);

		return response;
	}

	public PaymentFailResponse failPayment(String errorCode, String errorMessage, String bookingId) {
		Payment payment = getPaymentByBookingId(bookingId);
		payment.updateStatus(PaymentStatus.ABORTED);
		payment.updateFailedMsg(errorMessage);
		return new PaymentFailResponse(errorCode, errorMessage, bookingId);
	}

	public PaymentCancelResponse cancelPayment(String paymentKey, BookingCancelRequest request) {
		Payment payment = getPaymentByPaymentKey(paymentKey);
		PaymentCancelResponse response = tossPaymentService.requestTossPaymentCancellation(paymentKey, request);
		if (request.refundReceiveAccount().isPresent()) {
			RefundAccountRequest refundAccountRequest = request.refundReceiveAccount().get();
			payment.updateRefundInfo(
				refundAccountRequest.bank(),
				refundAccountRequest.accountNumber(),
				refundAccountRequest.holderName()
			);
		}
		Booking booking = payment.getBooking();
		payment.updateStatus(PaymentStatus.valueOf(response.status()));
		booking.updateStatus(BookingStatus.CANCELED);
		return response;
	}

	private Payment getPaymentByPaymentKey(String paymentKey) {
		return paymentRepository.findByPaymentKey(paymentKey)
			.orElseThrow(() -> new BookingException(BookingErrorCode.PAYMENT_NOT_FOUND));
	}

	private Payment getPaymentByBookingId(String bookingId) {
		return paymentRepository.findByBookingId(bookingId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.PAYMENT_NOT_FOUND));
	}
}
