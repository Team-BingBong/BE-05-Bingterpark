package com.pgms.apievent.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum EventErrorCode {

	EVENT_NOT_FOUND("NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 공연입니다."),
	EVENT_HALL_NOT_FOUND("EVENT HALL NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 공연장입니다."),
	ALREADY_EXIST_EVENT("DUPLICATE EVENT", HttpStatus.CONFLICT, "이미 존재하는 공연입니다."),
	EVENT_TIME_NOT_FOUND("EVENT TIME NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 회차입니다."),
	ALREADY_EXIST_EVENT_TIME("EVENT TIME ALREADY EXISTS", HttpStatus.CONFLICT, "공연에 대한 회차가 이미 존재합니다."),
	VALIDATION_FAILED("VALIDATION FAILED", HttpStatus.BAD_REQUEST, "입력값에 대한 검증에 실패했습니다.");

	private final String errorCode;
	private final HttpStatus status;
	private final String message;

	EventErrorCode(String errorCode, HttpStatus status, String message) {
		this.errorCode = errorCode;
		this.status = status;
		this.message = message;
	}
}
