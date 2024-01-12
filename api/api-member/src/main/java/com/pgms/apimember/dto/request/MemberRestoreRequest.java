package com.pgms.apimember.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRestoreRequest(
	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
	String email
) {
}
