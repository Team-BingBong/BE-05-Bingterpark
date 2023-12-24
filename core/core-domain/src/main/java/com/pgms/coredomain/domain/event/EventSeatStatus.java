package com.pgms.coredomain.domain.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventSeatStatus {
	AVAILABLE("예매가능"),
	IN_PROGRESS("예매중"),
	BOOKED("예매완료");

	private final String description;
}
