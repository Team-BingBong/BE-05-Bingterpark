package com.pgms.apibooking.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingErrorCode {
	SEAT_NOT_FOUND("SEAT_NOT_FOUND", "존재하지 않는 좌석입니다."),
	SEAT_BEING_BOOKED("SEAT_BEING_PURCHASED", "예매중인 좌석입니다."),

	INVALID_INPUT_VALUE("INVALID_INPUT_VALUE", "입력값을 확인해 주세요."),
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 에러");

	private final String code;
	private final String message;
}
