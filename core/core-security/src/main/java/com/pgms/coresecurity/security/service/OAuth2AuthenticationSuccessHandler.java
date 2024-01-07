package com.pgms.coresecurity.security.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.Role;
import com.pgms.coredomain.domain.member.enums.Provider;
import com.pgms.coredomain.domain.member.repository.MemberRepository;
import com.pgms.coredomain.domain.member.repository.RoleRepository;
import com.pgms.coresecurity.security.jwt.JwtUtils;
import com.pgms.coresecurity.security.util.HttpResponseUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final MemberRepository memberRepository;
	private final RoleRepository roleRepository;
	private final JwtUtils jwtUtils;

	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User)authentication.getPrincipal();

		Map<String, Object> properties = defaultOAuth2User.getAttribute("properties");
		String name = (String)properties.get("nickname");

		Map<String, Object> kakaoAccount = defaultOAuth2User.getAttribute("kakao_account");
		String email = (String)kakaoAccount.get("email");

		Member member = createOrUpdateMember(email, name);

		UserDetails userDetails = UserDetailsImpl.from(member);
		Authentication authenticated = new UsernamePasswordAuthenticationToken(
			userDetails,
			"",
			userDetails.getAuthorities());

		// 토큰 생성 후 반환
		Map<String, Object> body = new HashMap<>();
		body.put("accessToken", jwtUtils.generateJwtToken(authenticated));
		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, body);
	}

	private Member createOrUpdateMember(String email, String name) {
		return memberRepository.findByEmailWithRole(email).orElseGet(() -> {
			Role defaultRole = roleRepository.findByName("ROLE_USER") //TODO: Role Enum으로 변경
				.orElseThrow(() -> new RuntimeException("ROLE_USER가 존재하지 않습니다."));
			Member newMember = Member.builder()
				.email(email)
				.name(name)
				.provider(Provider.KAKAO)
				.role(defaultRole)
				.build();
			return memberRepository.save(newMember);
		});
	}
}
