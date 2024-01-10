package com.pgms.apibooking.domain.payment.dto.response;

import com.pgms.coredomain.domain.booking.Payment;

public record PaymentVirtualResponse(
	// 토스 응답이라 이름변경x
	String accountNumber,
	String bankCode,
	String customerName,
	String dueDate
) {

	public static PaymentVirtualResponse from(Payment payment) {
		return new PaymentVirtualResponse(
			payment.getAccountNumber(),
			payment.getBankCode().getBankNumCode(),
			payment.getDepositorName(),
			payment.getDueDate().toLocalDate().toString()
		);
	}
}
