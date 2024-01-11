package com.pgms.coresecurity.security.jwt;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.pgms.coresecurity.security.service.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.expiry-seconds}")
	private int expirySeconds;

	public String generateAccessToken(UserDetailsImpl userDetails) {
		Instant now = Instant.now();
		Instant expirationTime = now.plusSeconds(expirySeconds);

		String authorities = userDetails.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		return Jwts.builder()
			.claim("id", userDetails.getId())
			.subject((userDetails.getUsername()))
			.issuedAt(Date.from(now))
			.expiration(Date.from(expirationTime))
			.claim("authority", authorities)
			.signWith(key())
			.compact();
	}

	private SecretKey key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = Jwts.parser()
			.verifyWith(key())
			.build()
			.parseSignedClaims(accessToken)
			.getPayload();

		Collection<? extends GrantedAuthority> authorities =
			Arrays.stream(claims.get("authority").toString().split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();

		UserDetails principal = new UserDetailsImpl(claims.get("id", Long.class), claims.getSubject(), null,
			authorities);
		return new UsernamePasswordAuthenticationToken(principal, null, authorities);
	}

	public void validateAccessToken(String authToken) {
		Jwts.parser().verifyWith(key()).build().parse(authToken);
	}

	public String generateRefreshToken() {
		return UUID.randomUUID().toString();
	}
}
