package com.pgms.coresecurity.security.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.repository.AdminRepository;
import com.pgms.coresecurity.security.exception.SecurityCustomException;

import lombok.RequiredArgsConstructor;

@Service
@Qualifier("adminUserDetailsService")
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

	private final AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByEmail(email)
			.orElseThrow(() -> new SecurityCustomException(MemberErrorCode.ADMIN_NOT_FOUND));
		if (admin.isLocked())
			throw new SecurityCustomException(MemberErrorCode.NOT_ACTIVE_ADMIN);
		if (admin.isDeleted())
			throw new SecurityCustomException(MemberErrorCode.ADMIN_ALREADY_DELETED);
		return UserDetailsImpl.from(admin);
	}
}
