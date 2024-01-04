package com.pgms.apibooking.fake;

import java.time.LocalDateTime;
import java.util.List;

import com.pgms.apibooking.dto.request.BookingCancelRequest;
import com.pgms.apibooking.dto.request.PaymentConfirmRequest;
import com.pgms.apibooking.dto.response.PaymentCancelDetailResponse;
import com.pgms.apibooking.dto.response.PaymentCancelResponse;
import com.pgms.apibooking.dto.response.PaymentCardResponse;
import com.pgms.apibooking.dto.response.PaymentSuccessResponse;
import com.pgms.apibooking.dto.response.PaymentVirtualResponse;
import com.pgms.apibooking.dto.response.RefundAccountResponse;
import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;
import com.pgms.apibooking.service.TossPaymentService;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.repository.BookingRepository;
import com.pgms.coredomain.domain.booking.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TossPaymentServiceFake implements TossPaymentService {

	private static final LocalDateTime NOW = LocalDateTime.now();

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
					NOW.toString(),
					NOW.toString(),
					new PaymentCardResponse(
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
					NOW.toString(),
					NOW.toString(),
					null,
					new PaymentVirtualResponse(
						"X6505636518308",
						"20",
						"박토스",
						NOW.plusDays(7).toString()
					)
				);
			}
			default -> {
				return null;
			}
		}
	}

	@Override
	public PaymentCancelResponse requestTossPaymentCancellation(String paymentKey, BookingCancelRequest request) {
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
							request.cancelAmount(),
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
