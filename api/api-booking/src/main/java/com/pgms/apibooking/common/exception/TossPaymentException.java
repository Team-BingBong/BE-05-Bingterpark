package com.pgms.apibooking.common.exception;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public class TossPaymentException extends RuntimeException {
	private final String code;
	private final HttpStatus status;

	public TossPaymentException(ErrorResponse errorResponse, HttpStatus status) {
		super(errorResponse.getErrorMessage());
		this.code = errorResponse.getErrorCode();
		this.status = status;
	}
}
