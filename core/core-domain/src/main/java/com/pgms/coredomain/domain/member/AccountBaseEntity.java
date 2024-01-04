package com.pgms.coredomain.domain.member;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public class AccountBaseEntity extends BaseEntity {

	@Column(name = "last_login_at", nullable = false)
	protected LocalDateTime lastLoginAt = LocalDateTime.now();

	@Column(name = "last_password_updated_at", nullable = false)
	protected LocalDateTime lastPasswordUpdatedAt = LocalDateTime.now();

	public void updateLastLoginAt() {
		this.lastLoginAt = LocalDateTime.now();
	}

	public void updateLastPasswordUpdatedAt() {
		this.lastPasswordUpdatedAt = LocalDateTime.now();
	}
}
