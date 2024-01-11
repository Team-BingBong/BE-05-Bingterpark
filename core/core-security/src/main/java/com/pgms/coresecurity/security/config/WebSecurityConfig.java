package com.pgms.coresecurity.security.config;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.*;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.pgms.coredomain.domain.member.enums.Role;
import com.pgms.coresecurity.security.jwt.JwtAccessDeniedHandler;
import com.pgms.coresecurity.security.jwt.JwtAuthenticationEntryPoint;
import com.pgms.coresecurity.security.jwt.JwtAuthenticationFilter;
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

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	/**
	 * permitAll 권한을 가진 엔드포인트에 적용되는 SecurityFilterChain 입니다.
	 */
	@Bean
	public SecurityFilterChain securityFilterChainPermitAll(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.securityMatchers(matchers -> matchers
				.requestMatchers(requestPermitAll())
			)
			.authorizeHttpRequests().anyRequest().permitAll();
		return http.build();
	}

	private RequestMatcher[] requestPermitAll() {
		List<RequestMatcher> requestMatchers = List.of(
			// MEMBER
			antMatcher("/api/*/auth/**"),
			antMatcher("/api/*/members/signup"),

			// SWAGGER
			antMatcher("/v3/api-docs/**"),
			antMatcher("/swagger-ui/**"),

			// BOOKING
			antMatcher(GET, "/api/*/payments/**"),
			antMatcher(POST, "/api/*/payments/**"),
			antMatcher("/bookings"),
			antMatcher("/payments")
		);
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	/**
	 * OAuth 관련 엔드포인트에 적용되는 SecurityFilterChain 입니다.
	 */
	@Bean
	public SecurityFilterChain securityFilterChainOAuth(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.securityMatchers(matchers -> matchers
				.requestMatchers(
					antMatcher("/login"),
					antMatcher("/login/oauth2/code/kakao"),
					antMatcher("/oauth2/authorization/kakao")
				))
			.authorizeHttpRequests().anyRequest().permitAll().and()

			.oauth2Login(oauth2Configurer -> oauth2Configurer
				.loginPage("/login")
				.successHandler(oauthSuccessHandler)
				.userInfoEndpoint()
				.userService(oAuth2UserService));
		return http.build();
	}

	/**
	 * 인증 및 인가가 필요한 엔드포인트에 적용되는 SecurityFilterChain 입니다.
	 */
	@Bean
	public SecurityFilterChain securityFilterChainAuthorized(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.securityMatchers(matchers -> matchers
				.requestMatchers(requestHasRoleUser())
				.requestMatchers(requestHasRoleAdmin())
				.requestMatchers(requestHasRoleSuperAdmin())
			)
			.authorizeHttpRequests(auth -> auth
				// TODO 엔드포인트별 authorize 통합
				.requestMatchers(requestHasRoleSuperAdmin()).hasAuthority(Role.ROLE_SUPERADMIN.name())
				.requestMatchers(requestHasRoleAdmin()).hasAuthority(Role.ROLE_ADMIN.name())
				.requestMatchers(requestHasRoleUser()).hasAuthority(Role.ROLE_USER.name()))
			.exceptionHandling(exception -> {
				exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
				exception.accessDeniedHandler(jwtAccessDeniedHandler);
			})
			.addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class);
		return http.build();
	}

	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> filterRegistration(JwtAuthenticationFilter filter) {
		FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
		registration.setEnabled(false);
		return registration;
	}

	private RequestMatcher[] requestHasRoleSuperAdmin() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/*/admin/management/**"));
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	private RequestMatcher[] requestHasRoleAdmin() {
		List<RequestMatcher> requestMatchers = List.of(
			antMatcher("/api/*/admin/**"));
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	private RequestMatcher[] requestHasRoleUser() {
		List<RequestMatcher> requestMatchers = List.of(
			// MEMBER
			antMatcher("/api/*/members/**"),

			// BOOKING
			antMatcher(GET, "/api/*/bookings/**"),
			antMatcher(POST, "/api/*/bookings/**"),
			antMatcher(GET, "/api/*/seats/**"),
			antMatcher(POST, "/api/*/seats/**")
		);
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	/**
	 * 위에서 정의된 엔드포인트 이외에는 denyAll 로 설정
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.authorizeHttpRequests().anyRequest().permitAll().and() // TODO 완성 후 denyAll 로 변경, exceptionHandling 삭제하기
			.exceptionHandling(exception -> {
				exception.authenticationEntryPoint(jwtAuthenticationEntryPoint);
				exception.accessDeniedHandler(jwtAccessDeniedHandler);
			});
		return http.build();
	}

	private void configureCommonSecuritySettings(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.anonymous().disable()
			.formLogin().disable()
			.httpBasic().disable()
			.rememberMe().disable()
			.logout().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
}
