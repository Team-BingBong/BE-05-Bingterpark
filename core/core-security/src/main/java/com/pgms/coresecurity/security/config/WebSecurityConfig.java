package com.pgms.coresecurity.security.config;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pgms.coresecurity.security.jwt.JwtAccessDeniedHandler;
import com.pgms.coresecurity.security.jwt.JwtAuthenticationEntryPoint;
import com.pgms.coresecurity.security.jwt.JwtAuthenticationFilter;
import com.pgms.coresecurity.security.jwt.booking.BookingExceptionHandlerFilter;
import com.pgms.coresecurity.security.jwt.booking.BookingJwtAuthFilter;
import com.pgms.coresecurity.security.service.OAuth2UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
	private final OAuth2UserService oAuth2UserService;
	private final AuthenticationSuccessHandler oauthSuccessHandler;
	private final BookingJwtAuthFilter bookingJwtAuthFilter;
	private final BookingExceptionHandlerFilter bookingExceptionHandlerFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.oauth2Login(oauth2Configurer -> oauth2Configurer
				.loginPage("/login")
				.successHandler(oauthSuccessHandler)
				.userInfoEndpoint()
				.userService(oAuth2UserService))
			.httpBasic(AbstractHttpConfigurer::disable)
			.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(auth -> auth
				// TODO 엔드포인트별 authorize 통합
				.requestMatchers(requestPermitAll()).permitAll()
				.requestMatchers(requestHasRoleSuperAdmin()).hasRole("SUPERADMIN")
				.requestMatchers(requestHasRoleAdmin()).hasRole("ADMIN")
				.requestMatchers(requestHasRoleUser()).hasRole("USER")
				.anyRequest().permitAll()) // 완성 후 denyAll 로 변경할 예정
			.exceptionHandling(exception -> {
				exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
				exception.accessDeniedHandler(jwtAccessDeniedHandler);
			})
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	private RequestMatcher[] requestPermitAll() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/v1/auth/**"),
			antMatcher("/api/v1/members/signup"));
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	private RequestMatcher[] requestHasRoleSuperAdmin() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/v1/admin/management/**"));
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	private RequestMatcher[] requestHasRoleAdmin() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/v1/admin/**"));
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	private RequestMatcher[] requestHasRoleUser() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/v1/members/**"),
			// 예매
			antMatcher(GET, "/api/*/payments/**"),
			antMatcher(POST, "/api/*/payments/**"),
			antMatcher(GET, "/api/*/bookings/**"),
			antMatcher(GET, "/api/*/payments/**"));
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	@Bean
	public SecurityFilterChain bookingFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(auth -> auth.requestMatchers(requestBookingFilterChain()).permitAll())
			.addFilterBefore(bookingJwtAuthFilter, BasicAuthenticationFilter.class)
			.addFilterBefore(bookingExceptionHandlerFilter, BookingJwtAuthFilter.class);
		return http.build();
	}

	private RequestMatcher[] requestBookingFilterChain() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/*/seats"),
			antMatcher("/api/*/seats/**/select"),
			antMatcher("/api/*/seats/**/deselect"),
			antMatcher("/api/*/bookings/**"));
		return requestMatchers.toArray(RequestMatcher[]::new);
	}
}
