package com.pgms.apibooking.config;

import java.util.List;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pgms.apibooking.exception.BookingExceptionHandlerFilter;
import com.pgms.apibooking.jwt.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;
	private final BookingExceptionHandlerFilter exceptionHandlerFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		List<RequestMatcher> permitAllMatchers = List.of(
			new AntPathRequestMatcher("/api/*/bookings/enter-queue", HttpMethod.POST.toString()),
			new AntPathRequestMatcher("/api/*/bookings/order-in-queue", HttpMethod.GET.toString())
		);

		return http
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PathRequest.toH2Console()).permitAll()
				.requestMatchers(permitAllMatchers.toArray(new RequestMatcher[0])).permitAll()
				.anyRequest().authenticated()
			)
			.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(exceptionHandlerFilter, JwtAuthFilter.class)
			.addFilterBefore(jwtAuthFilter, BasicAuthenticationFilter.class)
			.build();
	}
}
