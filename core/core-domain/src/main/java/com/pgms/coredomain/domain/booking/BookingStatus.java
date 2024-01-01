package com.pgms.coredomain.domain.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingStatus {

	WAITING_FOR_PAYMENT("결제대기"),
	PAYMENT_COMPLETED("결제완료"),
	CANCELED("취소");

	private final String description;
}
