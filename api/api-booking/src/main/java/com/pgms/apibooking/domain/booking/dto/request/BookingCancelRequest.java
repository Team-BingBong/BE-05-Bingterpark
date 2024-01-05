package com.pgms.apibooking.domain.booking.dto.request;

import java.util.Optional;

import com.pgms.apibooking.domain.payment.dto.request.RefundAccountRequest;
import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingCancel;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record BookingCancelRequest(

	@NotBlank(message = "[취소 사유]는 필수 입력값입니다.")
	String cancelReason,

	@Valid
	Optional<RefundAccountRequest> refundReceiveAccount
	) {

	public static BookingCancel toEntity(BookingCancelRequest request, Integer amount, String createdBy, Booking booking) {
		return BookingCancel.builder()
			.reason(request.cancelReason)
			.amount(amount)
			.createdBy(createdBy)
			.booking(booking)
			.build();
	}
}
