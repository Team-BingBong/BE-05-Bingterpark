package com.pgms.apimember.dto.response;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.member.Role;

public record RoleGetResponse(
	Long id,
	String name,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static RoleGetResponse from(Role role) {
		return new RoleGetResponse(role.getId(), role.getName(), role.getCreatedAt(), role.getUpdatedAt());
	}
}
