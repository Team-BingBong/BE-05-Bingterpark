package com.pgms.apimember.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberPasswordVerifyRequest(
	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 6, message = "비밀번호는 6자 이상 입력해주세요.")
	String password
) {
}
