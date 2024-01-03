package com.pgms.apibooking.exception;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.coredomain.response.ErrorResponse;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BookingExceptionHandlerFilter extends OncePerRequestFilter {
	private final ObjectMapper objectMapper;

	public BookingExceptionHandlerFilter() {
		this.objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (BookingException e) {
			BookingErrorCode bookingErrorCode = e.getErrorCode();
			sendFailResponse(response, bookingErrorCode);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			BookingErrorCode bookingErrorCode = BookingErrorCode.INTERNAL_SERVER_ERROR;
			sendFailResponse(response, bookingErrorCode);
		}
	}

	private void sendFailResponse(HttpServletResponse response, BookingErrorCode bookingErrorCode) throws IOException {
		response.setStatus(bookingErrorCode.getStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ErrorResponse errorResponse = new ErrorResponse(bookingErrorCode.getCode(), bookingErrorCode.getMessage());
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
}
