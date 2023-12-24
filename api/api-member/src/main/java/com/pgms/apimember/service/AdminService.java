package com.pgms.apimember.service;

import static com.pgms.apimember.exception.CustomErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.AdminCreateRequest;
import com.pgms.apimember.dto.request.AdminUpdateRequest;
import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.dto.response.MemberSummaryGetResponse;
import com.pgms.apimember.exception.AdminException;
import com.pgms.coredomain.domain.member.Admin;
import com.pgms.coredomain.domain.member.Role;
import com.pgms.coredomain.domain.member.repository.AdminRepository;
import com.pgms.coredomain.domain.member.repository.MemberRepository;
import com.pgms.coredomain.domain.member.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

	private final AdminRepository adminRepository;
	private final MemberRepository memberRepository;
	private final RoleRepository roleRepository;

	// 슈퍼 관리자 기능
	public Long createAdmin(AdminCreateRequest requestDto) {
		checkPasswordMatched(requestDto.password(), requestDto.passwordConfirm());

		if (isAdminExistsByEmail(requestDto.email()))
			throw new AdminException(DUPLICATED_ADMIN_EMAIL);

		// TODO: 비밀번호 암호화 추가 필요
		final Admin admin = AdminCreateRequest.toEntity(requestDto, "암호화된 비밀번호", getRole(requestDto.roleName()));
		return adminRepository.save(admin).getId();
	}

	@Transactional(readOnly = true)
	public List<AdminGetResponse> getAdmins() {
		return adminRepository.findAll().stream().map(AdminGetResponse::from).toList();
	}

	public void updateAdmin(Long adminId, AdminUpdateRequest requestDto) {
		checkPasswordMatched(requestDto.password(), requestDto.passwordConfirm());

		final Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new AdminException(ADMIN_NOT_FOUND));

		final Role role = getRole(requestDto.roleName());
		// TODO: 비밀번호 암호화 추가 필요
		admin.update(requestDto.name(), "암호화된 비밀번호", requestDto.phoneNumber(), requestDto.status(), role);
	}

	// 일반 관리자 기능
	@Transactional(readOnly = true)
	public List<MemberSummaryGetResponse> getMembers() {
		return memberRepository.findAll().stream().map(MemberSummaryGetResponse::from).toList();
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

	private void checkPasswordMatched(String password, String passwordConfirm) {
		if (!password.equals(passwordConfirm)) {
			throw new AdminException(NOT_MATCH_PASSWORD_CONFIRM);
		}
	}

	private Role getRole(String roleName) {
		// return null; // 테스트용
		return roleRepository.findByName(roleName)
			.orElseThrow(() -> new AdminException(ADMIN_ROLE_NOT_FOUND));
	}

}
