package com.pgms.apimember.dto.response;

import com.pgms.coredomain.domain.member.Role;

public record RoleGetResponse(Long id, String name) {
	public static RoleGetResponse from(Role role) {
		return new RoleGetResponse(role.getId(), role.getName());
	}
}
