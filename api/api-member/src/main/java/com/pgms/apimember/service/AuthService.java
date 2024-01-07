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
		String accessToken = jwtTokenProvider.generateJwtToken((UserDetailsImpl)authenticated.getPrincipal());
		String refreshToken = jwtTokenProvider.generateRefreshToken();

		// redis에 저장
		refreshTokenRepository.save(new RefreshToken(refreshToken, accessToken, accountType,
			((UserDetailsImpl)authenticated.getPrincipal()).getEmail()));
		return new AuthResponse(accessToken, refreshToken);
	}

	public AuthResponse refresh(RefreshTokenRequest request) {
		// 기존에 저장된 refreshToken, refreshToken이 만료됐다면 다시 로그인하도록
		if (request.refreshToken() == null)
			throw new SecurityException(CustomErrorCode.UNAUTHORIZED);
		RefreshToken refreshToken = refreshTokenRepository.findById(request.refreshToken())
			.orElseThrow(() -> new SecurityException(CustomErrorCode.REFRESH_TOKEN_EXPIRED));

		// 사용자의 정보를 로드
		UserDetailsImpl userDetails;
		if (refreshToken.getAccountType().equals("admin")) {
			userDetails = (UserDetailsImpl)adminUserDetailsService.loadUserByUsername(refreshToken.getEmail());
		} else {
			userDetails = (UserDetailsImpl)memberUserDetailsService.loadUserByUsername(refreshToken.getEmail());
		}

		// 새로운 accessToken 발급
		String newAccessToken = jwtTokenProvider.generateJwtToken(userDetails);

		// redis에 저장
		refreshTokenRepository.save(new RefreshToken(refreshToken.getRefreshToken(), newAccessToken,
			refreshToken.getAccountType(), refreshToken.getEmail()));
		return new AuthResponse(newAccessToken, refreshToken.getRefreshToken());
	}
}
