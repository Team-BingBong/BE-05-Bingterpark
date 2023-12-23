package com.pgms.apimember.dto.response;

import com.pgms.coredomain.domain.member.Admin;

public record AdminGetResponse() {
	public static AdminGetResponse toDto(Admin admin) {
		return new AdminGetResponse();
	}
}
