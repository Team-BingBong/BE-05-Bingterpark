package com.pgms.coresecurity.security.jwt.booking;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pgms.coredomain.domain.common.BookingErrorCode;
import com.pgms.coresecurity.security.exception.SecurityCustomException;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookingJwtAuthFilter extends OncePerRequestFilter {

	private final BookingJwtProvider bookingJwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = parseToken(request);

		if (token != null) {
			try {
				BookingJwtPayload authentication = bookingJwtProvider.validateAndParsePayload(token);
				BookingAuthToken bookingAuthToken = new BookingAuthToken(authentication.sessionId());
				SecurityContextHolder.getContext().setAuthentication(bookingAuthToken);
			} catch (JwtException | SecurityCustomException e) {
				log.warn(e.getMessage(), e);
				throw new SecurityCustomException(BookingErrorCode.INVALID_BOOKING_TOKEN);
			}
		}

		filterChain.doFilter(request, response);
	}

	private String parseToken(HttpServletRequest request) {
		String headerAuth = request.getHeader("Booking-Authorization");
		if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}
}
