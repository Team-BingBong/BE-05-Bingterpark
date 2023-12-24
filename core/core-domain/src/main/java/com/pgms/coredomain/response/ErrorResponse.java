package com.pgms.coredomain.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

@Getter
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {
    private String errorCode; // ex) TOO_LONG_PRODUCT_DESCRIPTION
    private String errorMessage;
    private Map<String, String> validation = new HashMap<>();

    public ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public void addValidation(String field, String message) {
        validation.put(field, message);
    }
}
