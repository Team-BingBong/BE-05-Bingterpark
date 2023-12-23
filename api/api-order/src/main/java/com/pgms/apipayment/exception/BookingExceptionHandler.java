package com.pgms.apipayment.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class BookingExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
		HttpStatusCode statusCode, WebRequest request) {
		if (ex instanceof BindException) {
			BookingErrorCode errorCode = BookingErrorCode.INVALID_INPUT_VALUE;
			ErrorResponse response = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());

			((BindException)ex).getBindingResult().getAllErrors().forEach(e -> {
				String fieldName = ((FieldError)e).getField();
				String errorMessage = e.getDefaultMessage();
				response.addValidation(fieldName, errorMessage);
			});

			return ResponseEntity.badRequest().body(response);
		}

		log.error(ex.getMessage(), ex);

		BookingErrorCode errorCode = BookingErrorCode.INTERNAL_SERVER_ERROR;
		return ResponseEntity.internalServerError()
			.body(new ErrorResponse(errorCode.getCode(), errorCode.getMessage()));
	}
}
