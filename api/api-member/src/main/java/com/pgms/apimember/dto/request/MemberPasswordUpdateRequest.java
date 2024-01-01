package com.pgms.apimember.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberPasswordUpdateRequest(
	@NotBlank(message = "기존 비밀번호를 입력해주세요.")
	@Size(min = 6, message = "비밀번호는 6자 이상 입력해주세요.")
	String originPassword,

	@NotBlank(message = "새로운 비밀번호를 입력해주세요.")
	@Size(min = 6, message = "비밀번호는 6자 이상 입력해주세요.")
	String newPassword,

	@NotBlank(message = "비밀번호 확인을 입력해주세요.")
	String newPasswordConfirm
) {
}
