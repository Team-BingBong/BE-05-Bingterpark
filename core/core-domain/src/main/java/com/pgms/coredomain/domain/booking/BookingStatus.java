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

	public static BookingStatus fromDescription(String description) {
		for (BookingStatus bookingStatus : BookingStatus.values()) {
			if (bookingStatus.description.equals(description)) {
				return bookingStatus;
			}
		}
		throw new IllegalArgumentException("다음 예매 상태를 찾을 수 없습니다 : " + description);
	}
}
