package com.pgms.apimember.exception;

import com.pgms.coredomain.domain.common.MemberErrorCode;

import lombok.Getter;

@Getter
public class MemberException extends CustomException {

	public MemberException(MemberErrorCode errorCode) {
		super(errorCode);
	}
}
