package com.pgms.apimember.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminUpdateRequest(
	@NotBlank(message = "이름을 입력해주세요.")
	@Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
	String name,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Size(min = 6, message = "비밀번호는 6자 이상 입력해주세요.")
	String password,

	@NotBlank(message = "비밀번호 확인을 입력해주세요.")
	String passwordConfirm,

	@NotBlank(message = "전화번호를 입력해주세요.")
	@Pattern(regexp = "\\d+", message = "전화번호는 숫자만 입력해주세요.")
	String phoneNumber,

	@NotBlank(message = "역할을 입력해주세요.")
	String roleName
) {
}
