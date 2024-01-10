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
			ErrorResponse response = BookingErrorCode.INVALID_INPUT_VALUE.getErrorResponse();

			((BindException)ex).getBindingResult().getAllErrors().forEach(e -> {
				String fieldName = ((FieldError)e).getField();
				String errorMessage = e.getDefaultMessage();
				response.addValidation(fieldName, errorMessage);
			});

			return ResponseEntity.badRequest().body(response);
		}

		log.error(ex.getMessage(), ex);

		ErrorResponse response = BookingErrorCode.INTERNAL_SERVER_ERROR.getErrorResponse();
		return ResponseEntity.internalServerError().body(response);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ErrorResponse response = BookingErrorCode.INVALID_INPUT_VALUE.getErrorResponse();
		log.warn("HttpMessageNotReadableException Occurred : {}", response.getErrorMessage());
		return ResponseEntity.badRequest().body(response);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		BindingResult bindingResult = ex.getBindingResult();
		String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
		log.warn("Validation Failed: {}", errorMessage);
		ErrorResponse response = BookingErrorCode.INVALID_INPUT_VALUE.getErrorResponse();
		return ResponseEntity.status(status).body(response);
	}

	@ExceptionHandler(TossPaymentException.class)
	protected ResponseEntity<ErrorResponse> handleTossPaymentException(TossPaymentException ex) {
		ErrorResponse response = new ErrorResponse(ex.getCode(), ex.getMessage());
		log.warn("TossPaymentException Occurred : {}", response.getErrorMessage());
		return ResponseEntity.internalServerError().body(response);
	}

	@ExceptionHandler(BookingException.class)
	protected ResponseEntity<ErrorResponse> handleBookingException(BookingException ex) {
		ErrorResponse response = ex.getErrorCode().getErrorResponse();
		log.warn("Booking Exception Occurred : {}", response.getErrorMessage());
		return ResponseEntity.status(ex.getErrorCode().getStatus()).body(response);
	}
}
