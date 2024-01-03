package com.pgms.apibooking.jwt;

import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BookingJwtProvider {
	private final String issuer;
	private final SecretKey secretKey;
	private final Long expirySeconds;

	public String generateToken(BookingJwtPayload payload) {
		Long now = System.currentTimeMillis();
		Date expirationMilliSeconds = new Date(now + expirySeconds * 1000);

		return Jwts.builder()
			.issuer(issuer)
			.issuedAt(new Date(now))
			.expiration(expirationMilliSeconds)
			.claims(payload.toMap())
			.signWith(secretKey)
			.compact();
	}

	public BookingJwtPayload validateAndParsePayload(String token) {
		Claims claims = Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload();
		return new BookingJwtPayload(claims.get("memberId", Long.class));
	}
}
