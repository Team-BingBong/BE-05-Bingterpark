package com.pgms.apimember.exception;

public class SecurityException extends CustomException {
	public SecurityException(CustomErrorCode errorCode) {
		super(errorCode);
	}
}
