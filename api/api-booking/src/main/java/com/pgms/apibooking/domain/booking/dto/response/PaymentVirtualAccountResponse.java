package com.pgms.apibooking.domain.booking.dto.response;

import com.pgms.coredomain.domain.booking.BankCode;
import com.pgms.coredomain.domain.booking.Payment;

public record PaymentVirtualAccountResponse(
	String bank,
	String accountNumber,
	String customerName,
	String dueDate
) {

	public static PaymentVirtualAccountResponse from(Payment payment) {
		return new PaymentVirtualAccountResponse(
			BankCode.getByBankNumCode(payment.getBankCode().getBankNumCode()).toString(),
			payment.getAccountNumber(),
			payment.getDepositorName(),
			payment.getDueDate().toLocalDate().toString()
		);
	}
}
