package com.pgms.apimember.dto.response;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.enums.AccountStatus;
import com.pgms.coredomain.domain.member.enums.Gender;
import com.pgms.coredomain.domain.member.enums.Provider;
import com.pgms.coredomain.domain.member.enums.Role;

public record MemberDetailGetResponse(
	Long id,
	String name,
	String phoneNumber,
	String email,
	String birthDate,
	Gender gender,
	String streetAddress,
	String detailAddress,
	String zipCode,
	AccountStatus status,
	Provider provider,
	LocalDateTime lastLoginAt,
	LocalDateTime lastPasswordUpdatedAt,
	LocalDateTime createdAt,
	LocalDateTime updatedAt,
	Role role
) {
	public static MemberDetailGetResponse from(Member member) {
		return new MemberDetailGetResponse(
			member.getId(),
			member.getName(),
			member.getPhoneNumber(),
			member.getEmail(),
			member.getBirthDate(),
			member.getGender(),
			member.getStreetAddress(),
			member.getDetailAddress(),
			member.getZipCode(),
			member.getStatus(),
			member.getProvider(),
			member.getLastLoginAt(),
			member.getLastPasswordUpdatedAt(),
			member.getCreatedAt(),
			member.getUpdatedAt(),
			member.getRole()
		);
	}
}
