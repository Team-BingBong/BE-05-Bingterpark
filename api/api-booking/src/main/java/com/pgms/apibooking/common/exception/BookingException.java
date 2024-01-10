package com.pgms.apibooking.common.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class BookingException extends RuntimeException {

	private final BaseErrorCode errorCode;

	public BookingException(BaseErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
