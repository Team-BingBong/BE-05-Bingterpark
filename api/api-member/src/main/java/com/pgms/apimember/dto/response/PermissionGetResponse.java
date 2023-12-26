package com.pgms.apimember.dto.response;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.member.Permission;

public record PermissionGetResponse(
	Long id,
	String name,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
	public static PermissionGetResponse from(Permission permission) {
		return new PermissionGetResponse(permission.getId(), permission.getName(), permission.getCreatedAt(),
			permission.getUpdatedAt());
	}
}
