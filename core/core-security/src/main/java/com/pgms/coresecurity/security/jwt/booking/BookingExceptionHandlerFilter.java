package com.pgms.coresecurity.security.jwt.booking;

import static com.pgms.coredomain.domain.common.SecurityErrorCode.*;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgms.coredomain.domain.common.BaseErrorCode;
import com.pgms.coredomain.domain.common.BookingErrorCode;
import com.pgms.coredomain.response.ErrorResponse;
import com.pgms.coresecurity.security.exception.SecurityCustomException;
import com.pgms.coresecurity.security.util.HttpResponseUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BookingExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (SecurityCustomException e) {
			BaseErrorCode bookingErrorCode = e.getErrorCode();
			sendFailResponse(response, bookingErrorCode);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			BookingErrorCode bookingErrorCode = BookingErrorCode.INTERNAL_SERVER_ERROR;
			sendFailResponse(response, bookingErrorCode);
		}
	}

	private void sendFailResponse(HttpServletResponse response, BaseErrorCode bookingErrorCode) throws IOException {
		HttpResponseUtil.setErrorResponse(response, bookingErrorCode.getStatus(), bookingErrorCode.getErrorResponse());
	}
}
