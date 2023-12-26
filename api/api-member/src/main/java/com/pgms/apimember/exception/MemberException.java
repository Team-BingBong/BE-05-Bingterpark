package com.pgms.apimember.exception;

import lombok.Getter;

@Getter
public class MemberException extends CustomException {

	public MemberException(CustomErrorCode errorCode) {
		super(errorCode);
	}
}
