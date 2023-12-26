package com.pgms.apimember.exception;

import lombok.Getter;

@Getter
public class AdminException extends CustomException {

	public AdminException(CustomErrorCode errorCode) {
		super(errorCode);
	}
}
