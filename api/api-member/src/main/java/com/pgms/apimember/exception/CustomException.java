package com.pgms.apimember.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	protected final BaseErrorCode errorCode;

	public CustomException(BaseErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
