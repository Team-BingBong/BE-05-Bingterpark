package com.pgms.apibooking.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingErrorCode {
	SEAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "SEAT_NOT_FOUND", "존재하지 않는 좌석입니다."),
	SEAT_BEING_BOOKED(HttpStatus.BAD_REQUEST, "SEAT_BEING_BOOKED", "예매중인 좌석입니다."),
	SEAT_BOOKED(HttpStatus.BAD_REQUEST, "SEAT_BOOKED", "예매된 좌석입니다."),

	SEAT_SELECTION_REQUIRED(HttpStatus.BAD_REQUEST, "SEAT_SELECTION_REQUIRED", "하나 이상의 좌석을 선택해주세요."),
	EVENT_TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "EVENT_TIME_NOT_FOUND", "존재하지 않는 공연 회차입니다."),
	BOOKING_UNAVAILABLE(HttpStatus.BAD_REQUEST, "BOOKING_UNAVAILABLE", "현재 예매가 불가능한 공연 회차입니다."),
	EVENT_TIME_SEAT_MISMATCH(HttpStatus.BAD_REQUEST, "EVENT_TIME_SEAT_MISMATCH", "해당 공연 회차에서 구매가 불가능한 좌석이 포함되어 있습니다."),
	DELIVERY_ADDRESS_REQUIRED(HttpStatus.BAD_REQUEST, "DELIVERY_ADDRESS_REQUIRED", "배송지 정보를 입력해주세요."),

	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "입력값을 확인해 주세요."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 에러"),

	PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_NOT_FOUND", "존재하지 않는 결제입니다."),
	PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "PAYMENT_AMOUNT_MISMATCH", "결제 가격 정보가 일치하지 않습니다."),
	INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "INVALID_PAYMENT_METHOD", "결제 수단이 올바르지 않습니다."),
	TOSS_PAYMENTS_ERROR(HttpStatus.BAD_REQUEST, "TOSS_PAYMENTS_CLIENT_ERROR", "Toss Payments API 오류가 발생했습니다."),

	BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKING_NOT_FOUND", "존재하지 않는 예매입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;
}
