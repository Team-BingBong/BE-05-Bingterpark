package com.pgms.apimember.dto.response;

import com.pgms.coredomain.domain.member.Member;

public record MemberSummaryGetResponse() {
	public static MemberSummaryGetResponse toDto(Member member) {
		return new MemberSummaryGetResponse();
	}
}
