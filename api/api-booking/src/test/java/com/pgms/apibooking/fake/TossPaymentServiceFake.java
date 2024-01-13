package com.pgms.apibooking.fake;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.pgms.apibooking.domain.payment.dto.request.PaymentCancelRequest;
import com.pgms.apibooking.domain.payment.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCancelDetailResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentCardResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.domain.payment.dto.response.PaymentVirtualResponse;
import com.pgms.apibooking.domain.payment.dto.response.RefundAccountResponse;
import com.pgms.coredomain.domain.common.BookingErrorCode;
import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.domain.payment.service.TossPaymentService;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TossPaymentServiceFake implements TossPaymentService {

	OffsetDateTime NOW = OffsetDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

	private final BookingRepository bookingRepository;
	private final PaymentRepository paymentRepository;

	@Override
	public PaymentSuccessResponse requestTossPaymentConfirmation(PaymentConfirmRequest request) {
		Booking booking = bookingRepository.findWithPaymentById(request.orderId())
			.orElseThrow(() -> new BookingException(BookingErrorCode.BOOKING_NOT_FOUND));

		switch (booking.getPayment().getMethod()) {
			case CARD -> {
				return new PaymentSuccessResponse(
					request.paymentKey(),
					request.orderId(),
					booking.getBookingName(),
					booking.getPayment().getMethod().getDescription(),
					request.amount(),
					PaymentStatus.DONE.name(),
					NOW.format(formatter),
					NOW.format(formatter),
					new PaymentCardResponse(
						"61",
						"12341234****123*",
						0,
						false
					),
					null
				);
			}
			case VIRTUAL_ACCOUNT -> {
				return new PaymentSuccessResponse(
					request.paymentKey(),
					request.orderId(),
					booking.getBookingName(),
					booking.getPayment().getMethod().getDescription(),
					request.amount(),
					PaymentStatus.DONE.name(),
					NOW.format(formatter),
					NOW.format(formatter),
					null,
					new PaymentVirtualResponse(
						"X6505636518308",
						"20",
						"박토스",
						NOW.plusDays(7).format(formatter)
					)
				);
			}
			default -> {
				return null;
			}
		}
	}

	@Override
	public PaymentCancelResponse requestTossPaymentCancellation(String paymentKey, PaymentCancelRequest request) {
		Payment payment = paymentRepository.findByPaymentKey(paymentKey)
			.orElseThrow(() -> new BookingException(BookingErrorCode.PAYMENT_NOT_FOUND));

		switch (payment.getMethod()) {
			case CARD -> {
				return new PaymentCancelResponse(
					paymentKey,
					payment.getBooking().getId(),
					payment.getBooking().getBookingName(),
					payment.getMethod().getDescription(),
					payment.getAmount(),
					PaymentStatus.CANCELED.name(),
					NOW.toString(),
					NOW.toString(),
					new PaymentCardResponse(
						"61",
						"12341234****123*",
						0,
						false
					),
					null,
					List.of(
						new PaymentCancelDetailResponse(
							request.cancelReason(),
							request.cancelAmount(),
							NOW.toString()
						)
					)
				);
			}
			case VIRTUAL_ACCOUNT -> {
				int amount = payment.getAmount();
				if(payment.getStatus() == PaymentStatus.WAITING_FOR_DEPOSIT)
					amount = 0;
				return new PaymentCancelResponse(
					paymentKey,
					payment.getBooking().getId(),
					payment.getBooking().getBookingName(),
					payment.getMethod().getDescription(),
					payment.getAmount(),
					PaymentStatus.CANCELED.name(),
					NOW.toString(),
					NOW.toString(),
					null,
					new RefundAccountResponse(
						"20",
						"X6505831718354",
						"박토스"
					),
					List.of(
						new PaymentCancelDetailResponse(
							request.cancelReason(),
							amount,
							NOW.toString()
						)
					)
				);
			}
			default -> {
				return null;
			}
		}
	}
}
