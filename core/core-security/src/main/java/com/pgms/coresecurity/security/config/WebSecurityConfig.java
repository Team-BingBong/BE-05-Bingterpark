package com.pgms.coresecurity.security.config;

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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pgms.coresecurity.security.jwt.JwtAuthenticationEntryPoint;
import com.pgms.coresecurity.security.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

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
				// exception.accessDeniedHandler(customAccessDeniedHandler()); // TODO AccessDeniedHandler 추가
			})
			.addFilterBefore(jwtAuthenticationFilter,
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	private RequestMatcher[] requestPermitAll() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/v1/auth/**"));
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
			antMatcher("/api/v1/members/**"));
		return requestMatchers.toArray(RequestMatcher[]::new);
	}
}
