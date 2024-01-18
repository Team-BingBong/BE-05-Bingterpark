package com.pgms.coresecurity.security.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coresecurity.security.exception.SecurityCustomException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final PasswordEncoder passwordEncoder;
	private final UserDetailsService userDetailsService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();

		UserDetailsImpl userDetails = (UserDetailsImpl)userDetailsService.loadUserByUsername(email);

		if (userDetails.getProvider() != null) {
			throw new SecurityCustomException(MemberErrorCode.NOT_ALLOWED_BY_PROVIDER);
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new SecurityCustomException(MemberErrorCode.PASSWORD_NOT_MATCHED);
		}

		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
