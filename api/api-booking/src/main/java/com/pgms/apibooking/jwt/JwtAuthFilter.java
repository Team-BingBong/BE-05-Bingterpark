package com.pgms.apibooking.jwt;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		String token = parseToken(request);

		try {
			JwtPayload authentication = jwtProvider.validateAndParsePayload(token);
			BookingAuthToken bookingAuthToken = new BookingAuthToken(authentication.getMemberId());
			SecurityContextHolder.getContext().setAuthentication(bookingAuthToken);
		} catch (JwtException e) {
			throw new BookingException(BookingErrorCode.INVALID_BOOKING_TOKEN);
		} finally {
			filterChain.doFilter(request, response);
		}
	}

	private String parseToken(HttpServletRequest request) {
		String headerAuth = request.getHeader("Booking-Authorization");
		if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		throw new BookingException(BookingErrorCode.BOOKING_TOKEN_NOT_EXIST);
	}
}
