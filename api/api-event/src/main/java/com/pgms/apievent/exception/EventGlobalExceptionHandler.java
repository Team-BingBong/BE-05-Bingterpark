package com.pgms.apievent.exception;

import com.pgms.coredomain.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

import static com.pgms.apievent.exception.EventErrorCode.VALIDATION_FAILED;

@Slf4j
@RestControllerAdvice
public class EventGlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error(">>>>> Internal Server Error : {}", ex);
		ErrorResponse errorResponse = new ErrorResponse("INTERNAL SERVER ERROR", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	@ExceptionHandler(CustomException.class)
	protected ResponseEntity<ErrorResponse> handleEventCustomException(CustomException ex) {
		log.warn(">>>>> Custom Exception : {}", ex);
		EventErrorCode errorCode = ex.getErrorCode();
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getErrorCode(), errorCode.getMessage());
		return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		log.warn(">>>>> validation Failed : {}", ex);
		BindingResult bindingResult = ex.getBindingResult();
		String errorMessage = Objects.requireNonNull(bindingResult.getFieldError())
			.getDefaultMessage();

		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		ErrorResponse errorResponse = new ErrorResponse(VALIDATION_FAILED.getErrorCode(), errorMessage);
		fieldErrors.forEach(error -> errorResponse.addValidation(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
	}
}
