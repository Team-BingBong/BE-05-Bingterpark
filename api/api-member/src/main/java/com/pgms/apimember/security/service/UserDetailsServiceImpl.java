package com.pgms.apimember.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pgms.apimember.exception.AdminException;
import com.pgms.apimember.exception.CustomErrorCode;
import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByEmail(email)
			.orElseThrow(() -> new AdminException(CustomErrorCode.ADMIN_NOT_FOUND));

		return UserDetailsImpl.from(admin);
	}
}
