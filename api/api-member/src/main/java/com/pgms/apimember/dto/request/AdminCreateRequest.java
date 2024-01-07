package com.pgms.apimember.dto.request;

import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AdminCreateRequest(
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

	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
	String email,

	@NotBlank(message = "역할은 필수 항목입니다.")
	Role role
) {
	public static Admin toEntity(AdminCreateRequest requestDto, String encodedPassword, Role role) {
		return Admin.builder()
			.name(requestDto.name())
			.password(encodedPassword)
			.phoneNumber(requestDto.phoneNumber())
			.email(requestDto.email())
			.role(role)
			.build();
	}
}
