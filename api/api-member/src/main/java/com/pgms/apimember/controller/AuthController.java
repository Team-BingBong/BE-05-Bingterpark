package com.pgms.apimember.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apimember.dto.request.LoginRequest;
import com.pgms.apimember.dto.response.LoginResponse;
import com.pgms.apimember.security.JwtUtils;
import com.pgms.apimember.security.UserDetailsImpl;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtUtils jwtUtils;

	/**
	 * 로그인, 토큰 발급
	 */

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
		// 인증 전의 auth 객체
		Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.email(),
			loginRequest.password());

		// 인증 후의 auth 객체
		Authentication authenticated = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// jwt 생성
		String jwt = jwtUtils.generateJwtToken(authenticated);

		UserDetailsImpl userDetails = (UserDetailsImpl)authenticated.getPrincipal();
		return ResponseEntity.ok(ApiResponse.ok(new LoginResponse(jwt,
			userDetails.getId(),
			userDetails.getEmail(),
			userDetails.getAuthorities().stream().findFirst().get().getAuthority())));
	}

	/*
		토큰 재발급
	 */

}
