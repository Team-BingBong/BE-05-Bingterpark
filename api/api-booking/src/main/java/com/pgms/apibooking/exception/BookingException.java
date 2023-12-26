package com.pgms.apibooking.exception;

import lombok.Getter;

@Getter
public class BookingException extends RuntimeException {

	private final BookingErrorCode errorCode;

	public BookingException(BookingErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
