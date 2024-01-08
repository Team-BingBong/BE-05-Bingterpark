package com.pgms.apimember.exception;

import com.pgms.coredomain.domain.common.MemberErrorCode;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

	protected final MemberErrorCode errorCode;

	public CustomException(MemberErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
