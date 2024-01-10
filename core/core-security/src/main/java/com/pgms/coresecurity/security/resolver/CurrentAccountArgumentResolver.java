package com.pgms.coresecurity.security.resolver;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.pgms.coredomain.domain.common.SecurityErrorCode;
import com.pgms.coresecurity.security.exception.SecurityCustomException;
import com.pgms.coresecurity.security.service.UserDetailsImpl;

public class CurrentAccountArgumentResolver implements HandlerMethodArgumentResolver {

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
		UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
		return userDetails.getId();
	}

	private void validateAuthentication(Authentication authentication) {
		if (Objects.isNull(authentication) || !(authentication instanceof UsernamePasswordAuthenticationToken)) {
			throw new SecurityCustomException(SecurityErrorCode.UNAUTHORIZED);
		}
	}
}
