package com.pgms.apibooking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pgms.apibooking.common.jwt.BookingJwtProvider;
import io.jsonwebtoken.security.Keys;

import lombok.Getter;

@Getter
@Configuration
public class BookingJwtConfig {

	@Value("${booking-jwt.issuer}")
	private String issuer;

	@Value("${booking-jwt.secret-key}")
	private String secretKey;

	@Value("${booking-jwt.expiry-seconds}")
	private Long expirySeconds;

	@Bean
	public BookingJwtProvider jwtProvider() {
		return new BookingJwtProvider(
			issuer,
			Keys.hmacShaKeyFor(secretKey.getBytes()),
			expirySeconds
		);
	}
}
