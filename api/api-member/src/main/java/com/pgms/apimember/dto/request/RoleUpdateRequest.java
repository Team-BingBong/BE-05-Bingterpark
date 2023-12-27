package com.pgms.apimember.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleUpdateRequest(
	@NotBlank(message = "이름을 입력해주세요.")
	@Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
	String name
) {
}
