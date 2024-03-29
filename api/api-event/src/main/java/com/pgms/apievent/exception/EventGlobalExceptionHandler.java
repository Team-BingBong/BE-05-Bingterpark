package com.pgms.apievent.exception;

import com.pgms.coredomain.domain.common.BaseErrorCode;
import com.pgms.coredomain.response.ErrorResponse;
import com.pgms.coresecurity.security.exception.SecurityCustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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

	@ExceptionHandler(EventException.class)
	protected ResponseEntity<ErrorResponse> handleEventCustomException(EventException ex) {
		log.warn(">>>>> Custom Exception : {}", ex);
		BaseErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
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

	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<ErrorResponse> handleEventIllegalArgumentException(IllegalArgumentException ex) {
		log.warn(">>>>> IllegalArgument Exception : {}", ex);
		ErrorResponse errorResponse = new ErrorResponse("ILLEGAL ARGUMENT ERROR", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(InvalidDataAccessApiUsageException.class)
	protected ResponseEntity<ErrorResponse> handleInvalidDataAccessApiUsageException(InvalidDataAccessApiUsageException ex) {
		log.warn(">>>>> InvalidDataAccessApiUsageException Exception : {}", ex);
		ErrorResponse errorResponse = new ErrorResponse("ILLEGAL ARGUMENT ERROR", ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}

	@ExceptionHandler(SecurityCustomException.class)
	protected ResponseEntity<ErrorResponse> handleSecurityCustomException(SecurityCustomException ex) {
		log.warn(">>>>> SecurityCustomException : {}", ex);
		BaseErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus()).body(errorCode.getErrorResponse());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
		log.error(">>>>> Internal Server Error : {}", ex);
		ErrorResponse errorResponse = new ErrorResponse("INTERNAL SERVER ERROR", ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}
}
