package com.pgms.apibooking.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.apibooking.common.jwt.BookingJwtPayload;
import com.pgms.apibooking.common.jwt.BookingJwtProvider;
import com.pgms.coredomain.domain.common.BookingErrorCode;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookingTokenInterceptor implements HandlerInterceptor {

	private final BookingJwtProvider bookingJwtProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String token = parseToken(request);
		try {
			BookingJwtPayload authentication = bookingJwtProvider.validateAndParsePayload(token);
			request.setAttribute("tokenSessionId", authentication.sessionId());
		} catch (JwtException | BookingException e) {
			log.warn(e.getMessage(), e);
			throw new BookingException(BookingErrorCode.INVALID_BOOKING_TOKEN);
		}
		return true;
	}

	private String parseToken(HttpServletRequest request) {
		String headerAuth = request.getHeader("Booking-Authorization");
		if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		throw new BookingException(BookingErrorCode.BOOKING_TOKEN_NOT_EXIST);
	}
}
