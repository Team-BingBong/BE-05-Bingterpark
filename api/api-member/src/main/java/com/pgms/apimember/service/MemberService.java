package com.pgms.apimember.service;

import org.springframework.stereotype.Service;

import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.exception.CustomErrorCode;
import com.pgms.apimember.exception.MemberException;
import com.pgms.coredomain.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public MemberDetailGetResponse getMemberDetail(Long memberId) {
		return MemberDetailGetResponse.from(
			memberRepository.findByIdWithRole(memberId).
				orElseThrow(() -> new MemberException(CustomErrorCode.MEMBER_NOT_FOUND)));
	}
}
