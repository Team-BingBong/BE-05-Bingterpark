package com.pgms.coresecurity.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final PasswordEncoder passwordEncoder;
	private final AdminUserDetailsService adminUserDetailsService;
	private final MemberUserDetailsService memberUserDetailsService;

	@Autowired
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

		UserDetails userDetails;
		if (accountType.equals("member")) {
			userDetails = memberUserDetailsService.loadUserByUsername(email);
		} else {
			userDetails = adminUserDetailsService.loadUserByUsername(email);
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
