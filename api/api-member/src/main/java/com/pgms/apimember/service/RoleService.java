package com.pgms.apimember.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.RoleCreateRequest;
import com.pgms.apimember.dto.request.RoleUpdateRequest;
import com.pgms.apimember.dto.response.RoleGetResponse;
import com.pgms.apimember.exception.AdminException;
import com.pgms.apimember.exception.CustomErrorCode;
import com.pgms.coredomain.domain.member.Role;
import com.pgms.coredomain.domain.member.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

	private final RoleRepository roleRepository;

	public Long createRole(RoleCreateRequest request) {
		Role role = roleRepository.save(request.toEntity());
		return role.getId();
	}

	public List<RoleGetResponse> getRolesByIds(List<Long> ids) {
		return roleRepository.findAllById(ids).stream()
			.map(RoleGetResponse::from)
			.toList();
	}

	public void updateRole(Long id, RoleUpdateRequest request) {
		Role role = roleRepository.findById(id)
			.orElseThrow(() -> new AdminException(CustomErrorCode.ADMIN_ROLE_NOT_FOUND));
		role.changeName(request.name());
	}

	public void deleteRole(Long id) {
		Role role = roleRepository.findById(id)
			.orElseThrow(() -> new AdminException(CustomErrorCode.ADMIN_ROLE_NOT_FOUND));
		roleRepository.delete(role);
	}
}
