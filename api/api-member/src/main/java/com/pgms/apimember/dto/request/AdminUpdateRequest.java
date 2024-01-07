package com.pgms.apimember.dto.request;

import com.pgms.coredomain.domain.member.enums.AccountStatus;
import com.pgms.coredomain.domain.member.enums.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminUpdateRequest(
	@NotBlank(message = "이름은 필수 항목입니다.")
	@Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
	String name,

	@NotBlank(message = "비밀번호는 필수 항목입니다.")
	@Size(min = 6, message = "비밀번호는 6자 이상 입력해주세요.")
	String password,

	@NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
	String passwordConfirm,

	@NotBlank(message = "전화번호는 필수 항목입니다.")
	@Pattern(regexp = "\\d+", message = "전화번호는 숫자만 입력해주세요.")
	String phoneNumber,

	@NotBlank(message = "계정 상태는 필수 항목입니다.")
	AccountStatus status,

	@NotBlank(message = "역할을 입력해주세요.")
	Role role
) {
}
