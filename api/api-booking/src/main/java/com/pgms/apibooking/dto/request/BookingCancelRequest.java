package com.pgms.apibooking.dto.request;

import java.util.Optional;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.BookingCancel;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record BookingCancelRequest(
	@NotBlank(message = "[취소 사유]는 필수 입력값입니다.")
	String cancelReason,

	@Valid
	Optional<RefundAccountRequest> refundReceiveAccount
	) {

	public static BookingCancel toEntity(BookingCancelRequest request, String createdBy, Booking booking) {
		return BookingCancel.builder()
			.reason(request.cancelReason)
			.amount(booking.getAmount())
			.createdBy(createdBy)
			.booking(booking)
			.build();
	}
}
