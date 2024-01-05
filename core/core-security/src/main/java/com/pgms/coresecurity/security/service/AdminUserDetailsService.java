package com.pgms.coresecurity.security.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.repository.AdminRepository;

import lombok.RequiredArgsConstructor;

@Service
@Qualifier("adminUserDetailsService")
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

	private final AdminRepository adminRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByEmail(email)
			.orElseThrow(() -> new RuntimeException("해당 어드민이 존재하지 않습니다."));

		return UserDetailsImpl.from(admin);
	}
}
