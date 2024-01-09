package com.pgms.coresecurity.security.jwt.booking;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class BookingAuthToken extends AbstractAuthenticationToken {

	private final String sessionId;

	public BookingAuthToken(String sessionId) {
		super(null);
		this.sessionId = sessionId;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.sessionId;
	}
}
