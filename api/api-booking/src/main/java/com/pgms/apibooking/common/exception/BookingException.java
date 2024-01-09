package com.pgms.apibooking.common.exception;

import com.pgms.coredomain.domain.common.BookingErrorCode;

import lombok.Getter;

@Getter
public class BookingException extends RuntimeException {

	private final BookingErrorCode errorCode;

	public BookingException(BookingErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
