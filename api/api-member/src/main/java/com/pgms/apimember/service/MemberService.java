package com.pgms.apimember.service;

import static com.pgms.apimember.exception.CustomErrorCode.*;

import org.springframework.stereotype.Service;

import com.pgms.apimember.dto.request.MemberPasswordUpdateRequest;
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
		final Member member = getAvailableMember(memberId);
		validatePlainPassword(password, member.getPassword());
	}

	public void updatePassword(Long memberId, MemberPasswordUpdateRequest requestDto) {
		final Member member = getAvailableMember(memberId);
		validatePlainPassword(requestDto.originPassword(), member.getPassword());
		validatePasswordAndConfirm(requestDto.newPassword(), requestDto.newPasswordConfirm());
		member.updatePassword(requestDto.newPassword()); // TODO: 비밀번호 암호화 추가 필요

	}

	private void validatePlainPassword(String plainPassword, String encodedPassword) {
		// TODO: 비밀번호 암호화 추가 필요
		// encoder.matches(plainPassword, encodedPassword);
		if (!plainPassword.equals(encodedPassword)) { // 암호화 없는 임시 검증
			throw new MemberException(PASSWORD_NOT_MATCHED);
		}
	}

	private void validatePasswordAndConfirm(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(PASSWORD_CONFIRM_NOT_MATCHED);
		}
	}

	private Member getAvailableMember(Long memberId) {
		final Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

		// 필터단에서 검증 시 필요하지 않을수도 있음
		if (member.isDeleted()) {
			throw new MemberException(MEMBER_ALREADY_DELETED);
		}
		return member;
	}
}
