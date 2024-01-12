package com.pgms.coresecurity.security.config;

import static com.pgms.coredomain.domain.member.enums.Role.*;
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
			antMatcher("/api/*/members/send-restore-email"),
			antMatcher("/api/*/members/confirm-restore"),

			// BOOKING
			antMatcher(GET, "/api/*/payments/**"),
			antMatcher(POST, "/api/*/payments/**"),
			antMatcher("/bookings"),
			antMatcher("/payments"),

			// EVENT
			antMatcher(GET, "/api/*/events/*"),               // 공연 아이디 조회
			antMatcher(GET, "/api/*/events/sort/ranking"),    // 공연 목록 조회 - 랭킹순
			antMatcher(GET, "/api/*/events/sort/review"),     // 공연 목록 조회 - 리뷰순
			antMatcher(GET, "/api/*/events/sort/ended-at"),   // 공연 목록 조회 - 예약마감일자순
			antMatcher(GET, "/api/*/events/search/keyword"),  // 공연 키워드 검색
			antMatcher(GET, "/api/*/events/search/top-ten"),  // 실시간 검색어 순위
			antMatcher(GET, "/api/*/event-times/*"),             // 회차 아이디로 회차 정보 조회
			antMatcher(GET, "/api/*/event-times/events/*"),   // 공연 아이디로 특정 공연에 대한 전체 회차 정보 조회
			antMatcher(GET, "/api/*/event-halls/*"),             // 공연장 아이디 조회
			antMatcher(GET, "/api/*/event-halls"),             // 공연장 목록 조회
			antMatcher(GET, "/api/*/event-seats/event-times/*/seats"),    // 회차별 공연 좌석 조회
			antMatcher(GET, "/api/*/event-seats/event-times/*/available-numbers"),    // 회차별 공연 좌석 구역별 남은 자리 수 조회
			antMatcher(GET, "/api/*/events/*/seat-area"),    // 공연 좌석 구역 목록 조회

			// DOCS
			antMatcher("/swagger-ui/**"),
			antMatcher("/swagger-ui"),
			antMatcher("/swagger-ui.html"),
			antMatcher("/swagger/**"),
			antMatcher("/swagger-resources/**"),
			antMatcher("/v3/api-docs/**"),
			antMatcher("/webjars/**"),

			// H2-CONSOLE
			antMatcher("/h2-console/**")
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
				.requestMatchers(requestHasRoleSuperAdmin()).hasAuthority(ROLE_SUPERADMIN.name())
				.requestMatchers(requestHasRoleAdmin()).hasAuthority(ROLE_ADMIN.name())
				.requestMatchers(requestHasRoleUser()).hasAuthority(ROLE_USER.name()))
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
			// MEMBER
			antMatcher("/api/*/admin/**"),

			// EVENT
			antMatcher(POST, "/api/*/events"),                // 공연 생성
			antMatcher(PUT, "/api/*/events/*"),               // 공연 수정
			antMatcher(DELETE, "/api/*/events/*"),            // 공연 삭제
			antMatcher(POST, "/api/*/event-times/*"),         // 공연 회차 생성
			antMatcher(PATCH, "/api/*/event-times/*"),         // 회차 아이디로 회차 정보 수정
			antMatcher(DELETE, "/api/*/event-times/*"),        // 회차 아이디로 회차 삭제
			antMatcher(POST, "/api/*/event-halls"),            // 공연장 생성
			antMatcher(DELETE, "/api/*/event-halls/*"),        // 공연장 삭제
			antMatcher(PUT, "/api/*/event-halls/*"),           // 공연장 수정
			antMatcher(POST, "/api/*/event-seats/events/*"),   // 공연 좌석 생성
			antMatcher(PATCH, "/api/*/event-seats/seat-area"),   // 공연 좌석 등급 일괄 수정
			antMatcher(DELETE, "/api/*/event-seats"),            // 공연 좌석 일괄 삭제
			antMatcher(POST, "/api/*/events/*/seat-area"),       // 공연 좌석 구역 생성
			antMatcher(DELETE, "/api/*/events/seat-area/*"),     // 공연 좌석 구역 삭제
			antMatcher(PUT, "/api/*/events/seat-area/*"),        // 공연 좌석 구역 수정
			antMatcher(POST, "/api/*/thumbnails/events/*"),      // 공연 썸네일 이미지 생성
			antMatcher(PATCH, "/api/*/thumbnails/events/*"),     // 공연 썸네일 이미지 수정
			antMatcher(POST, "/api/*/event-images/events/*"),    // 공연 상세 이미지 추가
			antMatcher(DELETE, "/api/*/event-images/events/*")   // 공연 상세 이미지 삭제
		);
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
			antMatcher(POST, "/api/*/seats/**"),

			// EVENT
			antMatcher(POST, "/api/*/event-reviews/*"), // 공연 후기 생성
			antMatcher(PATCH, "/api/*/event-reviews/*"), // 공연 후기 수정
			antMatcher(GET, "/api/*/event-reviews/*"), // 리뷰 아이디로 조회
			antMatcher(GET, "/api/*/event-reviews/events/*"), // 특정 공연에 대한 리뷰 전체 조회
			antMatcher(DELETE, "/api/*/event-reviews/*") // 리뷰 아이디로 삭제
		);
		return requestMatchers.toArray(RequestMatcher[]::new);
	}

	/**
	 * 위에서 정의된 엔드포인트 이외에는 authenticated 로 설정
	 */
	@Bean
	public SecurityFilterChain securityFilterChainDefault(HttpSecurity http) throws Exception {
		configureCommonSecuritySettings(http);
		http
			.authorizeHttpRequests()
			.anyRequest().authenticated()
			.and()
			.addFilterAfter(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
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
			.headers().frameOptions().disable().and()
			.logout().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}
}
