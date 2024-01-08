package com.pgms.apibooking.common.exception;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.coredomain.response.ErrorResponse;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BookingAuthEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper;

	public BookingAuthEntryPoint() {
		this.objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		BookingErrorCode errorCode = BookingErrorCode.BOOKING_TOKEN_NOT_EXIST;
		response.setStatus(errorCode.getStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
