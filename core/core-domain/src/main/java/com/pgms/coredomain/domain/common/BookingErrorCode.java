package com.pgms.coredomain.domain.common;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BookingErrorCode implements BaseErrorCode{

	// seat
	SEAT_NOT_FOUND(HttpStatus.BAD_REQUEST, "SEAT_NOT_FOUND", "존재하지 않는 좌석입니다."),
	SEAT_ALREADY_BOOKED(HttpStatus.BAD_REQUEST, "SEAT_ALREADY_BOOKED", "예매된 좌석입니다."),
	SEAT_HELD_BY_ANOTHER_MEMBER(HttpStatus.BAD_REQUEST, "SEAT_HELD_BY_ANOTHER_MEMBER", "예매중인 좌석입니다."),
	SEAT_HOLDING_EXPIRED(HttpStatus.BAD_REQUEST, "SEAT_HOLDING_EXPIRED", "좌석 선점 시간이 만료되었습니다."),

	// booking
	BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOKING_NOT_FOUND", "존재하지 않는 예매입니다."),
	// booking - create
	TIME_NOT_FOUND(HttpStatus.BAD_REQUEST, "TIME_NOT_FOUND", "존재하지 않는 공연 회차입니다."),
	UNBOOKABLE_EVENT(HttpStatus.BAD_REQUEST, "UNBOOKABLE_EVENT", "현재 예매가 불가능한 공연입니다."),
	NON_EXISTENT_SEAT_INCLUSION(HttpStatus.BAD_REQUEST, "NON_EXISTENT_SEAT_INCLUSION", "선택한 공연 회차에 존재하지 않는 좌석이 포함되어 있습니다."),
	UNBOOKABLE_SEAT_INCLUSION(HttpStatus.BAD_REQUEST, "UNBOOKABLE_SEAT_INCLUSION", "예매가 불가능한 좌석이 포함되어 있습니다."),
	DELIVERY_ADDRESS_REQUIRED(HttpStatus.BAD_REQUEST, "DELIVERY_ADDRESS_REQUIRED", "배송지 정보를 입력해주세요."),
	// booking - cancel
	UNCANCELABLE_BOOKING(HttpStatus.BAD_REQUEST, "UNCANCELABLE_BOOKING", "취소할 수 없는 예매입니다."),
	REFUND_ACCOUNT_REQUIRED(HttpStatus.BAD_REQUEST, "REFUND_ACCOUNT_REQUIRED", "환불 받을 계좌 정보를 입력해주세요."),

	// bookingQueue
	OUT_OF_ORDER(HttpStatus.BAD_REQUEST, "OUT_OF_ORDER", "예매 순서가 아닙니다."),
	NOT_IN_QUEUE(HttpStatus.BAD_REQUEST, "NOT_IN_QUEUE", "대기열에 존재하지 않습니다."),
	BOOKING_SESSION_ID_NOT_EXIST(HttpStatus.BAD_REQUEST, "BOOKING_SESSION_ID_NOT_EXIST", "예매 세션 ID가 존재하지 않습니다."),
	BOOKING_TOKEN_NOT_EXIST(HttpStatus.UNAUTHORIZED, "BOOKING_TOKEN_NOT_EXIST", "예매 토큰이 존재하지 않습니다."),
	INVALID_BOOKING_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_BOOKING_TOKEN", "올바르지 않은 예매 토큰입니다."),

	// payment
	PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "PAYMENT_NOT_FOUND", "존재하지 않는 결제입니다."),
	PAYMENT_AMOUNT_MISMATCH(HttpStatus.BAD_REQUEST, "PAYMENT_AMOUNT_MISMATCH", "결제 가격 정보가 일치하지 않습니다."),
	INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST, "INVALID_PAYMENT_METHOD", "결제 수단이 올바르지 않습니다."),
	ACCOUNT_TRANSFER_ERROR(HttpStatus.BAD_REQUEST, "ACCOUNT_TRANSFER_ERROR", "계좌 송금 오류가 발생했습니다. 확인 후 다시 시도해주세요."),
	TOSS_PAYMENTS_ERROR(HttpStatus.BAD_REQUEST, "TOSS_PAYMENTS_CLIENT_ERROR", "Toss Payments API 오류가 발생했습니다."),

	// common
	FORBIDDEN(HttpStatus.FORBIDDEN, "BOOKER_NOT_SAME", "권한이 없습니다."),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE", "입력값을 확인해 주세요."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "서버 에러"),
	SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "SERVICE_UNAVAILABLE", "잠시 후 다시 시도해주세요.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(code, message);
	}
}
