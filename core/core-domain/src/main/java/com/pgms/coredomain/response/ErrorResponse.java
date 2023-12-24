package com.pgms.coredomain.response;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

	private final String errorCode; // ex) TOO_LONG_PRODUCT_DESCRIPTION
	private final String errorMessage;
	private final Map<String, String> validation = new HashMap<>();

	public ErrorResponse(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public void addValidation(String field, String message) {
		validation.put(field, message);
	}
}
