package com.pgms.coresecurity.security.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coresecurity.security.exception.SecurityCustomException;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final PasswordEncoder passwordEncoder;
	private final AdminUserDetailsService adminUserDetailsService;
	private final MemberUserDetailsService memberUserDetailsService;

	public CustomAuthenticationProvider(
		PasswordEncoder passwordEncoder,
		@Qualifier("adminUserDetailsService") AdminUserDetailsService adminUserDetailsService,
		@Qualifier("memberUserDetailsService") MemberUserDetailsService memberUserDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.adminUserDetailsService = adminUserDetailsService;
		this.memberUserDetailsService = memberUserDetailsService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String email = authentication.getName();
		String password = authentication.getCredentials().toString();
		String accountType = authentication.getDetails().toString();

		UserDetailsImpl userDetails;
		if (accountType.equals("member")) {
			userDetails = (UserDetailsImpl)memberUserDetailsService.loadUserByUsername(email);
		} else {
			userDetails = (UserDetailsImpl)adminUserDetailsService.loadUserByUsername(email);
		}

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
