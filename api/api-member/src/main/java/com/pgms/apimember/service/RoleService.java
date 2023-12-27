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
		validateRoleNameUnique(request.name());
		Role role = roleRepository.save(request.toEntity());
		return role.getId();
	}

	@Transactional(readOnly = true)
	public List<RoleGetResponse> getRolesByIds(List<Long> ids) {
		return roleRepository.findAllById(ids).stream()
			.map(RoleGetResponse::from)
			.toList();
	}

	public void updateRole(Long id, RoleUpdateRequest request) {
		Role role = roleRepository.findById(id)
			.orElseThrow(() -> new AdminException(CustomErrorCode.ADMIN_ROLE_NOT_FOUND));
		validateRoleNameUnique(request.name(), role.getId());
		role.changeName(request.name());
	}

	public void deleteRole(Long id) {
		Role role = roleRepository.findById(id)
			.orElseThrow(() -> new AdminException(CustomErrorCode.ADMIN_ROLE_NOT_FOUND));
		roleRepository.delete(role);
	}

	private void validateRoleNameUnique(String name) {
		roleRepository.findByName(name).ifPresent(r -> {
			throw new AdminException(CustomErrorCode.DUPLICATED_ROLE);
		});
	}

	private void validateRoleNameUnique(String name, Long id) {
		// 수정하려는 엔티티 자신의 id는 제외하고 중복 검사
		roleRepository.findByNameAndIdNot(name, id).ifPresent(r -> {
			throw new AdminException(CustomErrorCode.DUPLICATED_ROLE);
		});
	}
}
