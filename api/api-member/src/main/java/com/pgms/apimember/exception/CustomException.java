package com.pgms.apimember.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	protected final CustomErrorCode errorCode;

	public CustomException(CustomErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
