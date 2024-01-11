package com.pgms.apibooking.domain.payment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apibooking.domain.payment.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.domain.payment.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.domain.payment.dto.request.ConfirmVirtualIncomeRequest;
import com.pgms.apibooking.domain.payment.dto.request.RefundAccountRequest;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCardResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentFailResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentVirtualResponse;
import com.pgms.coredomain.domain.booking.CardIssuer;
import com.pgms.coredomain.domain.common.BookingErrorCode;
import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.common.util.DateTimeUtil;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingStatus;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
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
	private final TossPaymentService tossPaymentServiceImpl;

	public PaymentSuccessResponse successPayment(String paymentKey, String bookingId, int amount) {
		Booking booking = getBookingById(bookingId);
		Payment payment = booking.getPayment();

		if (payment.getAmount() != amount) {
			throw new BookingException(BookingErrorCode.PAYMENT_AMOUNT_MISMATCH);
		}
		PaymentConfirmRequest request = new PaymentConfirmRequest(paymentKey, bookingId, amount);
		PaymentSuccessResponse response = tossPaymentServiceImpl.requestTossPaymentConfirmation(request);
		payment.updateMethod(PaymentMethod.fromDescription(response.method()));

		switch (payment.getMethod()) {
			case CARD -> {
				PaymentCardResponse card = response.card();
				payment.updateCardInfo(
					CardIssuer.fromOfficialCode(card.issuerCode()),
					card.number(),
					card.installmentPlanMonths(),
					card.isInterestFree()
				);
				booking.updateStatus(BookingStatus.PAYMENT_COMPLETED);
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

		return response;
	}

	public PaymentFailResponse failPayment(String errorCode, String errorMessage, String bookingId) {//TODO : booking 상태값 변경
		Payment payment = getPaymentByBookingId(bookingId);
		payment.updateStatus(PaymentStatus.ABORTED);
		payment.updateFailedMsg(errorMessage);
		return new PaymentFailResponse(errorCode, errorMessage, bookingId);
	}

	public PaymentCancelResponse cancelPayment(String paymentKey, PaymentCancelRequest request) {
		Payment payment = getPaymentByPaymentKey(paymentKey);
		PaymentCancelResponse response = tossPaymentServiceImpl.requestTossPaymentCancellation(paymentKey, request);
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

	public void confirmVirtualAccountIncome(ConfirmVirtualIncomeRequest request) {
		Booking booking = getBookingById(request.orderId());
		Payment payment = booking.getPayment();

		switch (PaymentStatus.valueOf(request.status())) {
			case DONE -> {
				payment.updateApprovedAt(DateTimeUtil.parseNano(request.createdAt()));
				payment.updateStatus(PaymentStatus.DONE);
				booking.updateStatus(BookingStatus.PAYMENT_COMPLETED);
			}
			case WAITING_FOR_DEPOSIT -> throw new BookingException(BookingErrorCode.ACCOUNT_TRANSFER_ERROR);
			default -> throw new BookingException(BookingErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private Booking getBookingById(String bookingId) {
		Booking booking = bookingRepository.findWithPaymentById(bookingId)
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));
		return booking;
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
