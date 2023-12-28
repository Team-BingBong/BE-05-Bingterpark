package com.pgms.apimember.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pgms.apimember.dto.request.RoleCreateRequest;
import com.pgms.apimember.dto.request.RoleUpdateRequest;
import com.pgms.apimember.dto.response.RoleGetResponse;
import com.pgms.apimember.service.RoleService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/roles")
@RequiredArgsConstructor
public class RoleController {

	private final RoleService roleService;

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createRole(@RequestBody @Valid RoleCreateRequest request) {
		final Long roleId = roleService.createRole(request);
		final URI uri = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(roleId)
			.toUri();
		return ResponseEntity.created(uri).body(ApiResponse.created(roleId));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<RoleGetResponse>>> getAllPermissions() {
		List<RoleGetResponse> roles = roleService.getAllRoles();
		return ResponseEntity.ok(ApiResponse.ok(roles));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updateRole(@PathVariable Long id, @RequestBody @Valid RoleUpdateRequest request) {
		roleService.updateRole(id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
		roleService.deleteRole(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{roleId}/permissions")
	public ResponseEntity<ApiResponse<Long>> addPermissionToRole(@PathVariable Long roleId,
		@RequestParam Long permissionId) {
		roleService.addPermissionToRole(roleId, permissionId);
		return new ResponseEntity<>(ApiResponse.created(roleId), HttpStatus.CREATED);
	}

	@DeleteMapping("/{roleId}/permissions")
	public ResponseEntity<Void> removePermissionsFromRole(@PathVariable Long roleId, @RequestParam Long permissionId) {
		roleService.removePermissionFromRole(roleId, permissionId);
		return ResponseEntity.noContent().build();
	}
}
