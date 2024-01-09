package com.pgms.apibooking.config;

import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pgms.coresecurity.security.jwt.booking.BookingExceptionHandlerFilter;
import com.pgms.coresecurity.security.jwt.booking.BookingAuthEntryPoint;
import com.pgms.coresecurity.security.jwt.booking.BookingJwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final BookingJwtAuthFilter bookingJwtAuthFilter;
	private final BookingExceptionHandlerFilter bookingExceptionHandlerFilter;
	private final BookingAuthEntryPoint bookingAuthEntryPoint;

	@Bean
	public SecurityFilterChain bookingFilterChain(HttpSecurity http) throws Exception {
		List<RequestMatcher> permitAllMatchers = List.of(
			new AntPathRequestMatcher("/bookings", HttpMethod.GET.toString()),
			new AntPathRequestMatcher("/payments", HttpMethod.GET.toString()),
			new AntPathRequestMatcher("/api/*/bookings/issue-session-id", HttpMethod.POST.toString()),
			new AntPathRequestMatcher("/api/*/bookings/enter-queue", HttpMethod.POST.toString()),
			new AntPathRequestMatcher("/api/*/bookings/order-in-queue", HttpMethod.GET.toString()),
			new AntPathRequestMatcher("/api/*/bookings/exit-queue", HttpMethod.POST.toString()),
			new AntPathRequestMatcher("/api/*/bookings/issue-token", HttpMethod.POST.toString()),
			new AntPathRequestMatcher("/api/*/bookings/**/cancel", HttpMethod.POST.toString()),
			new AntPathRequestMatcher("/api/*/payments/**", HttpMethod.GET.toString()),
			new AntPathRequestMatcher("/api/*/payments/**", HttpMethod.POST.toString())
		);

		return http
			.csrf(AbstractHttpConfigurer::disable)
			.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PathRequest.toH2Console()).permitAll()
				.requestMatchers(permitAllMatchers.toArray(new RequestMatcher[0])).permitAll()
				.anyRequest().authenticated()
			)
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.authenticationEntryPoint(bookingAuthEntryPoint)
			)
			.sessionManagement(
				sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(bookingJwtAuthFilter, BasicAuthenticationFilter.class)
			.addFilterBefore(bookingExceptionHandlerFilter, BookingJwtAuthFilter.class)
			.build();
	}
}
