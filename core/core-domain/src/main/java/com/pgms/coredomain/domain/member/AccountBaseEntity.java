package com.pgms.coredomain.domain.member;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class AccountBaseEntity extends BaseEntity {

	@Column(name = "last_login_at", nullable = false)
	private LocalDateTime lastLoginAt;

	@Column(name = "last_password_updated_at", nullable = false)
	private LocalDateTime lastPasswordUpdatedAt;
}
