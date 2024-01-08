package com.pgms.apimember.exception;

import com.pgms.coredomain.domain.common.MemberErrorCode;

import lombok.Getter;

@Getter
public class AdminException extends CustomException {

	public AdminException(MemberErrorCode errorCode) {
		super(errorCode);
	}
}
