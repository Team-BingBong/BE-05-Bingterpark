package com.pgms.apimember.dto.request;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.Role;
import com.pgms.coredomain.domain.member.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MemberSignUpRequest(
	@NotBlank(message = "이메일은 필수 항목입니다.")
	@Email(message = "유효하지 않은 이메일 형식입니다.")
	String email,

	@NotBlank(message = "비밀번호는 필수 항목입니다.")
	@Size(min = 6, message = "비밀번호는 6자 이상 입력해주세요.")
	String password,

	@NotBlank(message = "비밀번호 확인은 필수 항목입니다.")
	String passwordConfirm,

	@NotBlank(message = "이름은 필수 항목입니다.")
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

	public Member toEntity(PasswordEncoder passwordEncoder, Role role) {
		return Member.builder()
			.email(email)
			.password(passwordEncoder.encode(password))
			.name(name)
			.phoneNumber(phoneNumber)
			.birthDate(birthDate)
			.gender(gender)
			.streetAddress(streetAddress)
			.detailAddress(detailAddress)
			.zipCode(zipCode)
			.role(role)
			.build();
	}
}
