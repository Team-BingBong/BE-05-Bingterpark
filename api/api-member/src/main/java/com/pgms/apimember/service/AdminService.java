package com.pgms.apimember.service;

import static com.pgms.apimember.exception.CustomErrorCode.*;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.AdminCreateRequest;
import com.pgms.apimember.dto.request.AdminUpdateRequest;
import com.pgms.apimember.dto.request.PageCondition;
import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.dto.response.MemberSummaryGetResponse;
import com.pgms.apimember.exception.AdminException;
import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.repository.AdminRepository;
import com.pgms.coredomain.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

	private final AdminRepository adminRepository;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	// 슈퍼 관리자 기능
	public Long createAdmin(AdminCreateRequest requestDto) {
		validatePasswordAndConfirm(requestDto.password(), requestDto.passwordConfirm());

		if (isAdminExistsByEmail(requestDto.email()))
			throw new AdminException(DUPLICATED_ADMIN_EMAIL);

		final Admin admin = AdminCreateRequest.toEntity(
			requestDto,
			passwordEncoder.encode(requestDto.password()),
			requestDto.role()
		);
		return adminRepository.save(admin).getId();
	}

	@Transactional(readOnly = true)
	public List<AdminGetResponse> getAdmins(PageCondition pageCondition) {
		Pageable pageable = PageRequest.of(pageCondition.getPage() - 1, pageCondition.getSize());
		return adminRepository.findSliceBy(pageable).stream().map(AdminGetResponse::from).toList();
	}

	public void updateAdmin(Long adminId, AdminUpdateRequest requestDto) {
		validatePasswordAndConfirm(requestDto.password(), requestDto.passwordConfirm());

		final Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new AdminException(ADMIN_NOT_FOUND));

		admin.update(
			requestDto.name(),
			passwordEncoder.encode(requestDto.password()),
			requestDto.phoneNumber(),
			requestDto.status(), requestDto.role()
		);
	}

	// 일반 관리자 기능
	@Transactional(readOnly = true)
	public List<MemberSummaryGetResponse> getMembers(PageCondition pageCondition) {
		Pageable pageable = PageRequest.of(pageCondition.getPage() - 1, pageCondition.getSize());
		return memberRepository.findSliceBy(pageable).stream().map(MemberSummaryGetResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public List<MemberDetailGetResponse> getMemberDetails(List<Long> memberIds) {
		return memberRepository.findAllById(memberIds).stream().map(MemberDetailGetResponse::from).toList();
	}

	@Transactional(readOnly = true)
	public AdminGetResponse getAdmin(Long adminId) {
		final Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new AdminException(ADMIN_NOT_FOUND));
		return AdminGetResponse.from(admin);
	}

	public void deleteAdmins(List<Long> adminIds) {
		List<Admin> admins = adminRepository.findAllById(adminIds);
		admins.forEach(admin -> {
			if (!admin.isDeleted()) {
				admin.updateToDeleted();
			}
		});
	}

	private boolean isAdminExistsByEmail(String email) {
		return adminRepository.existsByEmail(email);
	}

	private void validatePasswordAndConfirm(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new AdminException(PASSWORD_CONFIRM_NOT_MATCHED);
		}
	}
}
