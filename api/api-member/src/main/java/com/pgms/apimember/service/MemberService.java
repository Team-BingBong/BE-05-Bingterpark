package com.pgms.apimember.service;

import org.springframework.stereotype.Service;

import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.exception.CustomErrorCode;
import com.pgms.apimember.exception.MemberException;
import com.pgms.coredomain.domain.member.Member;
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

	public void verifyPassword(Long memberId, String password) {
		final Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(CustomErrorCode.MEMBER_NOT_FOUND));

		//TODO: 비밀번호 암호화 추가 필요
		final String encodedPassword = password + "암호화";
		if (!encodedPassword.equals(member.getPassword()))
			throw new MemberException(CustomErrorCode.PASSWORD_NOT_MATCHED);
	}
}
