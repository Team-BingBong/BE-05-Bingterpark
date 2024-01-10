package com.pgms.apibooking.domain.payment.dto.response;

import com.pgms.coredomain.domain.booking.Payment;

public record PaymentCardResponse(
	String number,
	int installmentPlanMonths,
	boolean isInterestFree
) {

	public static PaymentCardResponse from(Payment payment) {
		return new PaymentCardResponse(
			payment.getCardNumber(),
			payment.getInstallmentPlanMonths(),
			payment.isInterestFree()
		);
	}
}
