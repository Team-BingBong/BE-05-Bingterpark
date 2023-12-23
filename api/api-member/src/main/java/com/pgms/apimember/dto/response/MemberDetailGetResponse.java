package com.pgms.apimember.dto.response;

import com.pgms.coredomain.domain.member.Member;

public record MemberDetailGetResponse() {
	public static MemberDetailGetResponse toDto(Member member) {
		return new MemberDetailGetResponse();
	}
}
