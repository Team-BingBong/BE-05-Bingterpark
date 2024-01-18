package com.pgms.coresecurity.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.repository.MemberRepository;
import com.pgms.coresecurity.security.exception.SecurityCustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email)
			.orElseThrow(() -> new SecurityCustomException(MemberErrorCode.MEMBER_NOT_FOUND));
		if (member.isDeleted())
			throw new SecurityCustomException(MemberErrorCode.MEMBER_ALREADY_DELETED);
		return UserDetailsImpl.from(member);
	}
}
