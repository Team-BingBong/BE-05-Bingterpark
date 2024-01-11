package com.pgms.apimember.dto.request;

import com.pgms.coredomain.domain.member.enums.Gender;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdateRequest(
	@Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
	String name,

	@Pattern(regexp = "\\d+", message = "전화번호는 숫자만 입력해주세요.")
	String phoneNumber,

	@Pattern(regexp = "\\d{8}", message = "생년월일은 8자리 숫자만 입력해주세요.")
	String birthDate,

	Gender gender,

	@Size(max = 100, message = "도로명 주소는 최대 100자까지 입력할 수 있습니다.")
	String streetAddress,

	@Size(max = 100, message = "상세 주소는 최대 100자까지 입력할 수 있습니다.")
	String detailAddress,

	@Pattern(regexp = "\\d{5}", message = "우편번호는 5자리 숫자만 입력해주세요.")
	String zipCode
) {
}
