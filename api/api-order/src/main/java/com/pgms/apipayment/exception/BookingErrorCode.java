package com.pgms.apipayment.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingErrorCode {
	INVALID_INPUT_VALUE("INVALID_INPUT_VALUE", "입력값을 확인해 주세요."),
	INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버 에러");

	private final String code;
	private final String message;
}
