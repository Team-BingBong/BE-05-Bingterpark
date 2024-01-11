package com.pgms.apimember.service;

import static com.pgms.coredomain.domain.common.MemberErrorCode.*;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.MemberInfoUpdateRequest;
import com.pgms.apimember.dto.request.MemberPasswordUpdateRequest;
import com.pgms.apimember.dto.request.MemberSignUpRequest;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.exception.MemberException;
import com.pgms.coredomain.domain.common.MemberErrorCode;
import com.pgms.coredomain.domain.member.Member;
import com.pgms.coredomain.domain.member.redis.BlockedToken;
import com.pgms.coredomain.domain.member.redis.BlockedTokenRepository;
import com.pgms.coredomain.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final BlockedTokenRepository blockedTokenRepository;
	private final PasswordEncoder passwordEncoder;

	public Long signUp(MemberSignUpRequest requestDto) {
		if (memberRepository.existsByEmail(requestDto.email())) {
			throw new MemberException(DUPLICATED_MEMBER_EMAIL);
		}
		validateNewPassword(requestDto.password(), requestDto.passwordConfirm());
		return memberRepository.save(requestDto.toEntity(passwordEncoder.encode(requestDto.password()))).getId();
	}

	@Transactional(readOnly = true)
	public MemberDetailGetResponse getMemberDetail(Long memberId) {
		return MemberDetailGetResponse.from(
			memberRepository.findById(memberId).
				orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND)));
	}

	@Transactional(readOnly = true)
	public void verifyPassword(Long memberId, String password) {
		final Member member = getAvailableMember(memberId);
		validateStandardMember(member);
		validatePassword(password, member.getPassword());
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
		validateStandardMember(member);
		validatePassword(requestDto.originPassword(), member.getPassword());
		validateNewPassword(requestDto.newPassword(), requestDto.newPasswordConfirm());
		member.updatePassword(passwordEncoder.encode(requestDto.newPassword()));
	}

	public void deleteMember(Long memberId) {
		final Member member = getAvailableMember(memberId);
		member.updateToDeleted();
		blockedTokenRepository.save(
			new BlockedToken(getCurrentAccessToken())
		);
	}

	public Long restoreMember(Long memberId) {
		Member member = memberRepository.findByIdAndIsDeletedTrue(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
		member.updateToActive();
		return member.getId();
	}

	private void validatePassword(String plainPassword, String encodedPassword) {
		if (!encodedPassword.equals(passwordEncoder.encode(plainPassword))) {
			throw new MemberException(PASSWORD_NOT_MATCHED);
		}
	}

	private void validateNewPassword(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new MemberException(PASSWORD_CONFIRM_NOT_MATCHED);
		}
	}

	private void validateStandardMember(Member member) {
		if (member.isLoginByProvider()) {
			throw new MemberException(NOT_ALLOWED_BY_PROVIDER);
		}
	}

	private Member getAvailableMember(Long memberId) {
		return memberRepository.findByIdAndIsDeletedFalse(memberId)
			.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
	}

	private String getCurrentAccessToken() {
		return (String)SecurityContextHolder.getContext().getAuthentication().getCredentials();
	}
}
