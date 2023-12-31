package com.pgms.apibooking.dto.response;

public record RefundAccountResponse(
	String bankCode,
	String accountNumber,
	String holderName
) {
}
