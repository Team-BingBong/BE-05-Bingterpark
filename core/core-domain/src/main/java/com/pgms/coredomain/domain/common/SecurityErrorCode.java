package com.pgms.coredomain.domain.common;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum SecurityErrorCode implements BaseErrorCode {
	UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "로그인 해주세요."),
	ACCESS_TOKEN_EXPIRED("ACCESS TOKEN EXPIRED", HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),
	REFRESH_TOKEN_EXPIRED("REFRESH TOKEN EXPIRED", HttpStatus.UNAUTHORIZED, "다시 로그인 해주세요."),
	FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "권한이 없습니다");

	private final String errorCode;
	private final HttpStatus status;
	private final String message;

	SecurityErrorCode(String errorCode, HttpStatus status, String message) {
		this.errorCode = errorCode;
		this.status = status;
		this.message = message;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(errorCode, message);
	}
}
