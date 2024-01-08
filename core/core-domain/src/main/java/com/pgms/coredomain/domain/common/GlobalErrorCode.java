package com.pgms.coredomain.domain.common;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum GlobalErrorCode implements BaseErrorCode {
	INTERNAL_SERVER_ERROR("INTERNAL SERVER ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 발생했습니다. 잠시 후 다시 시도해주세요."),
	VALIDATION_FAILED("VALIDATION FAILED", HttpStatus.BAD_REQUEST, "입력값에 대한 검증에 실패했습니다.");

	private final String errorCode;
	private final HttpStatus status;
	private final String message;

	GlobalErrorCode(String errorCode, HttpStatus status, String message) {
		this.errorCode = errorCode;
		this.status = status;
		this.message = message;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(errorCode, message);
	}
}
