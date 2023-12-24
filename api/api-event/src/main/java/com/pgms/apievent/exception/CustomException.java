package com.pgms.apievent.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	private final EventErrorCode errorCode;

	public CustomException(EventErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
