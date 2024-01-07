package com.pgms.apibooking.domain.payment.dto.response;

public record RefundAccountResponse(
	String bankCode,
	String accountNumber,
	String holderName
) {
}
