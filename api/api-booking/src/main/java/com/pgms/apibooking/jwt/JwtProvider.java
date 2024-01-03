package com.pgms.apibooking.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JwtProvider {
	private final String issuer;
	private final SecretKey secretKey;
	private final Long expirySeconds;

	public String generateToken(JwtPayload payload) {
		Date now = new Date();
		Date expirationMilliSeconds = new Date(now.getTime() + expirySeconds * 1000);
		return Jwts.builder()
			.issuer(issuer)
			.issuedAt(now)
			.expiration(expirationMilliSeconds)
			.claims(payload.toMap())
			.signWith(secretKey)
			.compact();
	}

	public JwtPayload validateAndParsePayload(String token) {
		Claims claims = Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
		return new JwtPayload(claims.get("memberId", Long.class));
	}
}
