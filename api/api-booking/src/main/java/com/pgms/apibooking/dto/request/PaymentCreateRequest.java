package com.pgms.apibooking.dto.request;

import java.util.List;
import java.util.Optional;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.booking.Payment;
import com.pgms.coredomain.domain.booking.PaymentMethod;
import com.pgms.coredomain.domain.booking.PaymentStatus;
import com.pgms.coredomain.domain.booking.ReceiptType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record PaymentCreateRequest(

	@NotNull
	Long eventTimeId,

	@NotNull
	@NotEmpty
	List<Long> seatIds,

	@NotNull(message = "[수령 방법] 선택은 필수입니다.")
	ReceiptType receiptType,

	@NotBlank(message = "[구매자 명] 입력은 필수입니다.")
	String buyerName,

	@NotBlank(message = "[구매자 번호] 입력은 필수입니다.")
	String buyerPhoneNumber,

	@Valid
	Optional<DeliveryAddress> deliveryAddress,

	@NotNull(message = "[결제 수단] 선택은 필수입니다.")
	PaymentMethod method
) {

	public Payment toEntity(Booking booking) {
		return Payment.builder()
			.method(method)
			.booking(booking)
			.amount(booking.getAmount())
			.status(PaymentStatus.WAITING_FOR_DEPOSIT)
			.build();
	}
}
