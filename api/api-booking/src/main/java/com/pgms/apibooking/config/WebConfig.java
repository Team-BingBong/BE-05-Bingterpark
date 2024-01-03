package com.pgms.apibooking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.pgms.apibooking.interceptor.BookingSessionInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final BookingSessionInterceptor bookingSessionInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(bookingSessionInterceptor)
			.addPathPatterns("/api/*/bookings/enter-queue")
			.addPathPatterns("/api/*/bookings/order-in-queue")
			.addPathPatterns("/api/*/bookings/issue-token")
			.addPathPatterns("/api/*/bookings/exit-queue");
	}
}
