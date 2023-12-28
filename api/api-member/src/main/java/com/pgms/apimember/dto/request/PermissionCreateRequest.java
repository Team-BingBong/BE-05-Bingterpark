package com.pgms.apimember.dto.request;

import com.pgms.coredomain.domain.member.Permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PermissionCreateRequest(
	@NotBlank(message = "이름을 입력해주세요.")
	@Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
	String name
) {

	public Permission toEntity() {
		return new Permission(name);
	}
}
