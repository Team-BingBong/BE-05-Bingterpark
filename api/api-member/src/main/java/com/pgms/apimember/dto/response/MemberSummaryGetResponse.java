package com.pgms.apimember.dto.response;

import com.pgms.coredomain.domain.member.Member;

public record MemberSummaryGetResponse(
	Long id,
	String name,
	String phoneNumber,
	String email
) {
	public static MemberSummaryGetResponse from(Member member) {
		return new MemberSummaryGetResponse(
			member.getId(),
			member.getName(),
			member.getPhoneNumber(),
			member.getEmail()
		);
	}
}
