package com.pgms.apimember.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.LoginRequest;
import com.pgms.apimember.dto.response.LoginResponse;
import com.pgms.apimember.security.jwt.JwtUtils;
import com.pgms.apimember.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	public LoginResponse login(LoginRequest request) {
		// 인증 전의 auth 객체
		Authentication authentication = new UsernamePasswordAuthenticationToken(
			request.email(),
			request.password()
		);

		// 인증 후의 auth 객체
		Authentication authenticated = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// jwt 생성
		String jwt = jwtUtils.generateJwtToken(authenticated);

		UserDetailsImpl userDetails = (UserDetailsImpl)authenticated.getPrincipal();
		return new LoginResponse(jwt,
			userDetails.getId(),
			userDetails.getEmail(),
			userDetails.getAuthorities().stream().findFirst().get().getAuthority());
	}
}
