package com.pgms.apimember.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.dto.response.MemberSummaryGetResponse;
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

	@Transactional(readOnly = true)
	public Object getMembers(List<Long> memberIds) {
		if (memberIds == null || memberIds.isEmpty()) {
			return memberRepository.findAll().stream().map(MemberSummaryGetResponse::toDto).toList();
		} else {
			return memberRepository.findAllById(memberIds).stream().map(MemberDetailGetResponse::toDto).toList();
		}
	}

	@Transactional(readOnly = true)
	public AdminGetResponse getAdmin(Long adminId) {
		return AdminGetResponse.toDto(getAvailableAdmin(adminId));
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
}
