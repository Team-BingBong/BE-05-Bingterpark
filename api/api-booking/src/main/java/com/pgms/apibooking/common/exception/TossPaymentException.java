package com.pgms.apibooking.common.exception;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public class TossPaymentException extends RuntimeException {
	private final String code;

	public TossPaymentException(ErrorResponse errorResponse) {
		super(errorResponse.getErrorMessage());
		this.code = errorResponse.getErrorCode();
	}
}
