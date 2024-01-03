package com.pgms.apibooking.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pgms.apibooking.jwt.JwtProvider;

import lombok.Getter;

@Getter
@Configuration
public class JwtConfig {

	@Value("${jwt.issuer}")
	private String issuer;

	@Value("${jwt.secret-key}")
	private SecretKey secretKey;

	@Value("${jwt.expiry-seconds}")
	private Long expirySeconds;

	@Bean
	public JwtProvider jwtProvider() {
		return new JwtProvider(issuer, secretKey, expirySeconds);
	}
}
