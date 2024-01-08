package com.pgms.coredomain.domain.common;

import com.pgms.coredomain.response.ErrorResponse;

public interface BaseErrorCode {
	ErrorResponse getErrorResponse();

	String getMessage();
}
