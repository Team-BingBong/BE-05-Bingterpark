package com.pgms.apievent.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class EventException extends RuntimeException {

	private final BaseErrorCode errorCode;

	public EventException(BaseErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
