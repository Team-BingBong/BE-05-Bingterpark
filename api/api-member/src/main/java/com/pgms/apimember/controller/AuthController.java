package com.pgms.apimember.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apimember.dto.request.LoginRequest;
import com.pgms.apimember.dto.request.RefreshTokenRequest;
import com.pgms.apimember.dto.response.AuthResponse;
import com.pgms.apimember.service.AuthService;
import com.pgms.coredomain.domain.member.enums.Role;
import com.pgms.coredomain.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "인증", description = "인증 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	/**
	 * 로그인
	 */
	@Operation(summary = "관리자 로그인", description = "관리자 로그인 API 입니다.")
	@PostMapping("/admin/login")
	public ResponseEntity<ApiResponse<AuthResponse>> adminLogin(@Valid @RequestBody LoginRequest request) {
		AuthResponse response = authService.login(request, Role.ROLE_ADMIN);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	@Operation(summary = "회원 로그인", description = "회원 로그인 API 입니다.")
	@PostMapping("/members/login")
	public ResponseEntity<ApiResponse<AuthResponse>> memberLogin(@Valid @RequestBody LoginRequest request) {
		AuthResponse response = authService.login(request, Role.ROLE_USER);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}

	/**
	 * 토큰 재발급
	 */
	@Operation(summary = "토큰 재발급", description = "토큰 재발급 API 입니다.")
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshTokenRequest request) {
		AuthResponse response = authService.refresh(request);
		return ResponseEntity.ok(ApiResponse.ok(response));
	}
}
