package com.pgms.apimember.exception;

import com.pgms.coredomain.domain.common.MemberErrorCode;

public class SecurityException extends CustomException {
	public SecurityException(MemberErrorCode errorCode) {
		super(errorCode);
	}
}
