package com.pgms.coredomain.domain.booking;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BookingStatus {

	WAITING_FOR_DEPOSIT("입금대기"),
	PAYMENT_COMPLETED("결제완료"),
	CANCELLED("취소");

	private final String description;
}
