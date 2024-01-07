package com.pgms.apimember.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.LoginRequest;
import com.pgms.apimember.dto.request.RefreshTokenRequest;
import com.pgms.apimember.dto.response.AuthResponse;
import com.pgms.apimember.exception.CustomErrorCode;
import com.pgms.apimember.exception.SecurityException;
import com.pgms.apimember.redis.RefreshToken;
import com.pgms.apimember.redis.RefreshTokenRepository;
import com.pgms.coresecurity.security.jwt.JwtTokenProvider;
import com.pgms.coresecurity.security.service.AdminUserDetailsService;
import com.pgms.coresecurity.security.service.MemberUserDetailsService;
import com.pgms.coresecurity.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;
	private final AdminUserDetailsService adminUserDetailsService;
	private final MemberUserDetailsService memberUserDetailsService;

	public AuthResponse login(LoginRequest request, String accountType) {
		// 인증 전의 auth 객체
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			request.email(),
			request.password()
		);
		authentication.setDetails(accountType);

		// 인증 후의 auth 객체
		Authentication authenticated = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// accessToken, refreshToken 생성
		String accessToken = jwtTokenProvider.generateAccessToken((UserDetailsImpl)authenticated.getPrincipal());
		String refreshToken = jwtTokenProvider.generateRefreshToken();

		// redis에 토큰 정보 저장
		refreshTokenRepository.save(new RefreshToken(refreshToken, accessToken, accountType,
			((UserDetailsImpl)authenticated.getPrincipal()).getEmail()));
		return new AuthResponse(accessToken, refreshToken);
	}

	public AuthResponse refresh(RefreshTokenRequest request) {
		// refresh token이 만료됐는지 확인
		RefreshToken refreshToken = refreshTokenRepository.findById(request.refreshToken())
			.orElseThrow(() -> new SecurityException(CustomErrorCode.REFRESH_TOKEN_EXPIRED));

		// 회원 정보 로드
		UserDetailsImpl userDetails = loadUserDetails(refreshToken.getAccountType(), refreshToken.getEmail());

		// 새로운 accessToken, refreshToken 발급
		String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
		String newRefreshToken = jwtTokenProvider.generateRefreshToken();

		// 기존 refreshToken 삭제, redis에 토큰 정보 저장
		refreshTokenRepository.delete(refreshToken);
		refreshTokenRepository.save(new RefreshToken(newRefreshToken, newAccessToken,
			refreshToken.getAccountType(), refreshToken.getEmail()));
		return new AuthResponse(newAccessToken, refreshToken.getRefreshToken());
	}

	private UserDetailsImpl loadUserDetails(String accountType, String email) {
		if ("admin".equals(accountType)) {
			return (UserDetailsImpl)adminUserDetailsService.loadUserByUsername(email);
		} else {
			return (UserDetailsImpl)memberUserDetailsService.loadUserByUsername(email);
		}
	}
}
