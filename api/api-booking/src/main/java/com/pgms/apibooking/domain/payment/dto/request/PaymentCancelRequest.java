package com.pgms.apibooking.domain.payment.dto.request;

import java.util.Optional;

public record PaymentCancelRequest(
	String cancelReason,
	Integer cancelAmount,
	Optional<RefundAccountRequest> refundReceiveAccount
) {

	public static PaymentCancelRequest of(String cancelReason, Integer cancelAmount, Optional<RefundAccountRequest> refundReceiveAccount) {
		return new PaymentCancelRequest(cancelReason, cancelAmount, refundReceiveAccount);
	}
}
