package com.pgms.apibooking.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class BookingAuthToken extends AbstractAuthenticationToken {

	private final Long memberId;

	public BookingAuthToken(Long memberId) {
		super(null);
		this.memberId = memberId;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.memberId;
	}
}
