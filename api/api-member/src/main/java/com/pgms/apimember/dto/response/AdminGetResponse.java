package com.pgms.apimember.dto.response;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.Role;
import com.pgms.coredomain.domain.member.enums.AccountStatus;

public record AdminGetResponse(
	Long id,
	String name,
	String phoneNumber,
	String email,
	AccountStatus status,
	LocalDateTime lastLoginAt,
	LocalDateTime lastPasswordUpdatedAt,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	Role role
) {
	public static AdminGetResponse toDto(Admin admin) {
		return new AdminGetResponse(
			admin.getId(),
			admin.getName(),
			admin.getPhoneNumber(),
			admin.getEmail(),
			admin.getStatus(),
			admin.getLastLoginAt(),
			admin.getLastPasswordUpdatedAt(),
			admin.getCreatedAt(),
			admin.getUpdatedAt(),
			admin.getRole()
		);
	}
}
