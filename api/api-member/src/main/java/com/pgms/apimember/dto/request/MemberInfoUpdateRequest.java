package com.pgms.apimember.dto.request;

import com.pgms.coredomain.domain.member.enums.Gender;

public record MemberInfoUpdateRequest(
	//TODO: validation 회원가입이랑 통일해서 작성
	String name,
	String phoneNumber,
	String birthDate,
	Gender gender,
	String streetAddress,
	String detailAddress,
	String zipCode
) {
}
