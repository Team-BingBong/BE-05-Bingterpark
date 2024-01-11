package com.pgms.coresecurity.security.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.enums.Provider;

import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {

	private Long id;
	private String email;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Provider provider;

	public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities,
		Provider provider) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.provider = provider;
	}

	public static UserDetails from(Admin admin) {
		List<GrantedAuthority> authorities = admin.getRole() != null ?
			List.of(new SimpleGrantedAuthority(admin.getRole().name()))
			: null;
		return new UserDetailsImpl(admin.getId(), admin.getEmail(), admin.getPassword(), authorities);
	}

	public static UserDetails from(Member member) {
		List<GrantedAuthority> authorities = member.getRole() != null ?
			List.of(new SimpleGrantedAuthority(member.getRole().name()))
			: null;
		return new UserDetailsImpl(
			member.getId(),
			member.getEmail(),
			member.getPassword(),
			authorities,
			member.getProvider()
		);
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
