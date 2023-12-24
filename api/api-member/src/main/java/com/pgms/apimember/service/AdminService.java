package com.pgms.apimember.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.AdminCreateRequest;
import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.dto.response.MemberSummaryGetResponse;
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

	public Long createAdmin(AdminCreateRequest requestDto) {
		if (isAdminExistsByEmail(requestDto.email()))
			throw new NoSuchElementException("Admin already exists");

		if (!requestDto.password().equals(requestDto.passwordConfirm())) {
			throw new IllegalArgumentException("Password and passwordConfirm are not matched");
		}

		final Admin admin = AdminCreateRequest.toEntity(requestDto, getRole(requestDto.roleName()));
		return adminRepository.save(admin).getId();
	}

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
		return AdminGetResponse.from(getAvailableAdmin(adminId));
	}

	public void deleteAdmin(Long adminId) {
		final Admin admin = getAvailableAdmin(adminId);
		admin.updateToDeleted();
	}

	private Admin getAvailableAdmin(Long adminId) {
		final Admin admin = adminRepository.findById(adminId)
			.orElseThrow(() -> new NoSuchElementException("Admin not found"));
		if (admin.isDeleted()) {
			throw new IllegalArgumentException("Admin is already deleted");
		}
		return admin;
	}

	private boolean isAdminExistsByEmail(String email) {
		return adminRepository.existsByEmail(email);
	}

	private Role getRole(String roleName) {
		return roleRepository.findByName(roleName)
			.orElseThrow(() -> new NoSuchElementException("Role not found"));
	}
}
