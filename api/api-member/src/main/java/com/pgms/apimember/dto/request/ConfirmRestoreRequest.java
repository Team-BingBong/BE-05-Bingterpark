package com.pgms.apimember.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ConfirmRestoreRequest(
	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
	String email,

	@NotBlank(message = "인증 코드는 필수 항목입니다.")
	@Size(min = 6, max = 6, message = "인증 코드는 6자리 입니다.")
	String code
) {
}
