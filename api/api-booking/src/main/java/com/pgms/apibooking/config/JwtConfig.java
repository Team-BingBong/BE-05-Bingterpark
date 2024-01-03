package com.pgms.apibooking.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pgms.apibooking.jwt.BookingJwtProvider;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Getter
@Configuration
public class JwtConfig {

	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.expiry-seconds}")
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
