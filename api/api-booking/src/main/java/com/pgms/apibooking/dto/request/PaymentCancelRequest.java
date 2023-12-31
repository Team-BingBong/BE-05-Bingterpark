package com.pgms.apibooking.dto.request;

import java.util.Optional;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record PaymentCancelRequest(

	@NotBlank(message = "[취소 사유]는 필수 입력값입니다.")
	String cancelReason,

	@Nullable
	int cancelAmount,

	@Valid
	Optional<RefundAccountRequest> refundReceiveAccount
) {
}
