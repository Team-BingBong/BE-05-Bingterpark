package com.pgms.apibooking.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.pgms.apibooking.exception.BookingErrorCode;
import com.pgms.apibooking.exception.BookingException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class BookingSessionInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {
		String bookingSessionId = request.getHeader("Booking-Session-Id");
		if (bookingSessionId == null || bookingSessionId.isBlank()) {
			throw new BookingException(BookingErrorCode.BOOKING_SESSION_ID_NOT_EXIST);
		}

		request.setAttribute("bookingSessionId", bookingSessionId);
		return true;
	}
}
