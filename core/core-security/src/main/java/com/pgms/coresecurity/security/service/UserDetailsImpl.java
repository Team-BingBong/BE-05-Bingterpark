package com.pgms.coresecurity.security.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private Long id;
	private String email;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public static UserDetails from(Admin admin) {
		List<GrantedAuthority> authorities = admin.getRole() != null ?
			List.of(new SimpleGrantedAuthority(admin.getRole().name()))
			: Collections.emptyList();
		return new UserDetailsImpl(admin.getId(), admin.getEmail(), admin.getPassword(), authorities);
	}

	public static UserDetails from(Member member) {
		List<GrantedAuthority> authorities = member.getRole() != null ?
			List.of(new SimpleGrantedAuthority(member.getRole().name()))
			: Collections.emptyList();
		return new UserDetailsImpl(member.getId(), member.getEmail(), member.getPassword(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
