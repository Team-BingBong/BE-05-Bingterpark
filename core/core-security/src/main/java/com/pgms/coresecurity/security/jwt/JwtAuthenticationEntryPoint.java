package com.pgms.coresecurity.security.jwt;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.domain.common.SecurityErrorCode;
import com.pgms.coredomain.response.ErrorResponse;
import com.pgms.coresecurity.security.util.HttpResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 사용자가 인증되지 않은 상태에서 접근하려고 할 때 발생하는 예외 처리
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException)
		throws IOException {
		log.warn("Unauthorized: ", authException);

		ErrorResponse errorResponse;
		if (authException instanceof CredentialsExpiredException) {
			errorResponse = SecurityErrorCode.ACCESS_TOKEN_EXPIRED.getErrorResponse();
		} else {
			errorResponse = SecurityErrorCode.UNAUTHORIZED.getErrorResponse();
		}

		HttpResponseUtil.setErrorResponse(response, HttpStatus.UNAUTHORIZED, errorResponse);
	}
}
