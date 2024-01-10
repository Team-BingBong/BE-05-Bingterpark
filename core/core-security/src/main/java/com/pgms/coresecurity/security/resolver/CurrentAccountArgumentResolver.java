package com.pgms.coresecurity.security.resolver;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.pgms.coredomain.domain.common.SecurityErrorCode;
import com.pgms.coredomain.domain.member.redis.BlockedTokenRepository;
import com.pgms.coresecurity.security.exception.SecurityCustomException;
import com.pgms.coresecurity.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrentAccountArgumentResolver implements HandlerMethodArgumentResolver {

	private final BlockedTokenRepository blockedTokenRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean hasParameterAnnotation = parameter.hasParameterAnnotation(CurrentAccount.class);
		boolean hasLongParameterType = parameter.getParameterType().isAssignableFrom(Long.class);
		return hasParameterAnnotation && hasLongParameterType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		validateAuthentication(authentication);
		checkBlockedToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		return userDetails.getId();
	}

	private void validateAuthentication(Authentication authentication) {
		if (Objects.isNull(authentication) || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new SecurityCustomException(SecurityErrorCode.UNAUTHORIZED);
		}
	}

	private void checkBlockedToken(Authentication authentication) {
		String token = (String)authentication.getCredentials();
		blockedTokenRepository.findById(token)
			.ifPresent(blockedToken -> {
				log.warn("blocked token: ", blockedToken.getToken());
				throw new SecurityCustomException(SecurityErrorCode.FORBIDDEN);
			});
	}
}
