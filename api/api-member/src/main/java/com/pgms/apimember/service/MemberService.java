package com.pgms.apimember.service;

import static com.pgms.apimember.exception.CustomErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.MemberInfoUpdateRequest;
import com.pgms.apimember.dto.request.MemberPasswordUpdateRequest;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.exception.CustomErrorCode;
import com.pgms.apimember.exception.MemberException;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public MemberDetailGetResponse getMemberDetail(Long memberId) {
		return MemberDetailGetResponse.from(
			memberRepository.findByIdWithRole(memberId).
				orElseThrow(() -> new MemberException(CustomErrorCode.MEMBER_NOT_FOUND)));
	}

	@Transactional(readOnly = true)
	public void verifyPassword(Long memberId, String password) {
		final Member member = getAvailableMember(memberId);
		validatePlainPassword(password, member.getPassword());
	}

	public void updateMember(Long memberId, MemberInfoUpdateRequest requestDto) {
		final Member member = getAvailableMember(memberId);
		member.updateMemberInfo(
			requestDto.name(),
			requestDto.phoneNumber(),
			requestDto.birthDate(),
			requestDto.gender(),
			requestDto.streetAddress(),
			requestDto.detailAddress(),
			requestDto.zipCode()
		);
	}

	public void updatePassword(Long memberId, MemberPasswordUpdateRequest requestDto) {
		final Member member = getAvailableMember(memberId);
		validatePlainPassword(requestDto.originPassword(), member.getPassword());
		validatePasswordAndConfirm(requestDto.newPassword(), requestDto.newPasswordConfirm());
		member.updatePassword(requestDto.newPassword()); // TODO: 비밀번호 암호화 추가 필요

	}

	public void deleteMember(Long memberId) {
		final Member member = getAvailableMember(memberId);
		member.updateToDeleted();
	}

	public Long restoreMember(Long memberId) {
		Member member = memberRepository.findByIdAndIsDeletedTrue(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
		member.updateToActive();
		return member.getId();
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
		return memberRepository.findByIdAndIsDeletedFalse(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}

}
