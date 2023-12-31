package com.pgms.apibooking.common.exception;

import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pgms.coredomain.domain.common.BookingErrorCode;
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
		ErrorResponse response = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
		return ResponseEntity.internalServerError().body(response);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		BookingErrorCode errorCode = BookingErrorCode.INVALID_INPUT_VALUE;
		ErrorResponse response = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
		return ResponseEntity.badRequest().body(response);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		BindingResult bindingResult = ex.getBindingResult();
		String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
		log.warn("Validation Failed: {}", errorMessage);
		ErrorResponse response = new ErrorResponse(BookingErrorCode.INVALID_INPUT_VALUE.getCode(), errorMessage);
		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(BookingException.class)
	protected ResponseEntity<ErrorResponse> handleBookingException(BookingException ex) {
		ErrorResponse response = new ErrorResponse(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage());
		log.warn("Booking Exception Occurred : {}", response.getErrorMessage());
		return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
	}
}
