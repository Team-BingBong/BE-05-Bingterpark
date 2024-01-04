package com.pgms.apimember.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pgms.apimember.dto.request.AdminCreateRequest;
import com.pgms.apimember.dto.request.AdminUpdateRequest;
import com.pgms.apimember.dto.request.PageCondition;
import com.pgms.apimember.dto.request.PermissionCreateRequest;
import com.pgms.apimember.dto.request.PermissionUpdateRequest;
import com.pgms.apimember.dto.request.RoleCreateRequest;
import com.pgms.apimember.dto.request.RoleUpdateRequest;
import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.dto.response.PermissionGetResponse;
import com.pgms.apimember.dto.response.RoleGetResponse;
import com.pgms.apimember.service.AdminService;
import com.pgms.apimember.service.PermissionService;
import com.pgms.apimember.service.RoleService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/management")
@RequiredArgsConstructor
public class AdminManagementController { // 슈퍼관리자 전용 컨트롤러

	private final AdminService adminService;
	private final RoleService roleService;
	private final PermissionService permissionService;

	/**
	 * 관리자 CRUD
	 */

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createAdmin(@RequestBody @Valid AdminCreateRequest requestDto) {
		final Long adminId = adminService.createAdmin(requestDto);
		final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			.buildAndExpand(adminId)
			.toUri();
		return ResponseEntity.created(uri).body(ApiResponse.created(adminId));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<AdminGetResponse>>> getAdmins(
		@ModelAttribute @Valid PageCondition pageCondition) {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getAdmins(pageCondition)));
	}

	@PatchMapping("/{adminId}")
	public ResponseEntity<ApiResponse<Void>> updateAdmin(
		@PathVariable Long adminId,
		@RequestBody @NotNull AdminUpdateRequest requestDto) {
		adminService.updateAdmin(adminId, requestDto);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteAdmins(@RequestParam List<Long> adminIds) {
		adminService.deleteAdmins(adminIds);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 역할 CRUD
	 */

	@PostMapping("/roles")
	public ResponseEntity<ApiResponse<Long>> createRole(@RequestBody @Valid RoleCreateRequest request) {
		final Long roleId = roleService.createRole(request);
		final URI uri = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(roleId)
			.toUri();
		return ResponseEntity.created(uri).body(ApiResponse.created(roleId));
	}

	@GetMapping("/roles")
	public ResponseEntity<ApiResponse<List<RoleGetResponse>>> getAllRoles() {
		List<RoleGetResponse> roles = roleService.getAllRoles();
		return ResponseEntity.ok(ApiResponse.ok(roles));
	}

	@PatchMapping("/roles/{id}")
	public ResponseEntity<Void> updateRole(@PathVariable Long id, @RequestBody @Valid RoleUpdateRequest request) {
		roleService.updateRole(id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/roles/{id}")
	public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
		roleService.deleteRole(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/roles/{roleId}/permissions")
	public ResponseEntity<ApiResponse<Long>> addPermissionToRole(@PathVariable Long roleId,
		@RequestParam Long permissionId) {
		roleService.addPermissionToRole(roleId, permissionId);
		return new ResponseEntity<>(ApiResponse.created(roleId), HttpStatus.CREATED);
	}

	@DeleteMapping("/roles/{roleId}/permissions")
	public ResponseEntity<Void> removePermissionsFromRole(@PathVariable Long roleId, @RequestParam Long permissionId) {
		roleService.removePermissionFromRole(roleId, permissionId);
		return ResponseEntity.noContent().build();
	}

	/**
	 * 권한 CRUD
	 */

	@PostMapping("/permissions")
	public ResponseEntity<ApiResponse<Long>> createPermission(@RequestBody @Valid PermissionCreateRequest request) {
		final Long permissionId = permissionService.createPermission(request);
		final URI uri = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(permissionId)
			.toUri();
		return ResponseEntity.created(uri).body(ApiResponse.created(permissionId));
	}

	@GetMapping("/permissions")
	public ResponseEntity<ApiResponse<List<PermissionGetResponse>>> getAllPermissions() {
		List<PermissionGetResponse> permissions = permissionService.getAllPermissions();
		return ResponseEntity.ok(ApiResponse.ok(permissions));
	}

	@PatchMapping("/permissions/{id}")
	public ResponseEntity<Void> updatePermission(@PathVariable Long id,
		@RequestBody @Valid PermissionUpdateRequest request) {
		permissionService.updatePermission(id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/permissions/{id}")
	public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
		permissionService.deletePermission(id);
		return ResponseEntity.noContent().build();
	}
}
