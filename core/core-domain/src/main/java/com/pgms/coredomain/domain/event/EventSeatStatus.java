package com.pgms.coredomain.domain.event;

import lombok.Getter;

@Getter
public enum EventSeatStatus {
	AVAILABLE("예매가능"),
	SELECTED("예매중"),
	BOOKED("예매완료");

	private final String description;

	EventSeatStatus(String description) {
		this.description = description;
	}

	public static EventSeatStatus of(String input) {
		try {
			return EventSeatStatus.valueOf(input.toUpperCase());
		} catch (Exception e) {
			throw new IllegalArgumentException("존재하지 않는 좌석 상태입니다. : " + input);
		}
	}
}
