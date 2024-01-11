package com.pgms.apibooking.domain.payment.dto.response;

public record PaymentVirtualResponse(
	// 토스 응답이라 이름변경x
	String accountNumber,
	String bankCode,
	String customerName,
	String dueDate
) {
}
