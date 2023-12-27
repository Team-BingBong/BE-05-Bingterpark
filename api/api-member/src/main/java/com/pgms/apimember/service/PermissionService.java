package com.pgms.apimember.service;

import static com.pgms.apimember.exception.CustomErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apimember.dto.request.PermissionCreateRequest;
import com.pgms.apimember.dto.request.PermissionUpdateRequest;
import com.pgms.apimember.dto.response.PermissionGetResponse;
import com.pgms.apimember.exception.AdminException;
import com.pgms.coredomain.domain.member.Permission;
import com.pgms.coredomain.domain.member.repository.PermissionRepository;
import com.pgms.coredomain.domain.member.repository.RolePermissionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService {

	private final PermissionRepository permissionRepository;
	private final RolePermissionRepository rolePermissionRepository;

	public Long createPermission(PermissionCreateRequest request) {
		validatePermissionNameUnique(request.name());
		Permission permission = permissionRepository.save(request.toEntity());
		return permission.getId();
	}

	@Transactional(readOnly = true)
	public List<PermissionGetResponse> getAllPermissions() {
		return permissionRepository.findAll().stream()
			.map(PermissionGetResponse::from)
			.toList();
	}

	public void updatePermission(Long id, PermissionUpdateRequest request) {
		validatePermissionNameUnique(request.name(), id);
		Permission permission = permissionRepository.findById(id)
			.orElseThrow(() -> new AdminException(ADMIN_PERMISSION_NOT_FOUND));
		permission.updateName(request.name());
	}

	public void deletePermission(Long id) {
		if (rolePermissionRepository.countByPermissionId(id) > 0) {
			throw new AdminException(PERMISSION_ASSIGNED);
		}
		permissionRepository.deleteById(id);
	}

	private void validatePermissionNameUnique(String name) {
		permissionRepository.findByName(name).ifPresent(r -> {
			throw new AdminException(DUPLICATED_PERMISSION);
		});
	}

	private void validatePermissionNameUnique(String name, Long id) {
		// 수정하려는 엔티티 자신의 id는 제외하고 중복 검사
		permissionRepository.findByNameAndIdNot(name, id).ifPresent(r -> {
			throw new AdminException(DUPLICATED_PERMISSION);
		});
	}
}
