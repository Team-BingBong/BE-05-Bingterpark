package com.pgms.apibooking.dto.request;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentCreateRequest(

	@NotNull(message = "[결제 수단] 선택은 필수입니다.")
	PaymentMethod method,

	@NotNull(message = "[예매 ID]는 필수 입력값 입니다.")
	Long bookingId,

	@NotNull(message = "[결제 가격]은 필수 입력값 입니다.")
	@Positive(message = "[결제 가격]은 0보다 큰 값이어야 합니다.")
	int amount
) {
	public Payment toEntity(Booking booking) {
		return Payment.builder()
			.method(method)
			.booking(booking)
			.amount(amount)
			.status(PaymentStatus.WAITING_FOR_DEPOSIT)
			.build();
	}
}
