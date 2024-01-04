package com.pgms.apibooking.dto.request;

import java.util.Optional;

public record PaymentCancelRequest(
	String cancelReason,
	int cancelAmount,
	Optional<RefundAccountRequest> refundReceiveAccount
) {

	public static PaymentCancelRequest of(String cancelReason, int cancelAmount, Optional<RefundAccountRequest> refundReceiveAccount) {
		return new PaymentCancelRequest(cancelReason, cancelAmount, refundReceiveAccount);
	}
}
