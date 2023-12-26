package com.pgms.apimember.controller;

import java.net.URI;
import java.util.List;

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

import com.pgms.apimember.dto.request.PermissionCreateRequest;
import com.pgms.apimember.dto.request.PermissionUpdateRequest;
import com.pgms.apimember.dto.response.PermissionGetResponse;
import com.pgms.apimember.service.PermissionService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/permissions")
@RequiredArgsConstructor
public class PermissionController {

	private final PermissionService permissionService;

	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createPermission(@RequestBody @Valid PermissionCreateRequest request) {
		final Long permissionId = permissionService.createPermission(request);
		final URI uri = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(permissionId)
			.toUri();
		return ResponseEntity.created(uri).body(ApiResponse.created(permissionId));
	}

	@GetMapping("/details")
	public ResponseEntity<ApiResponse<List<PermissionGetResponse>>> getAllPermissions() {
		List<PermissionGetResponse> permissions = permissionService.getAllPermissions();
		return ResponseEntity.ok(ApiResponse.ok(permissions));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<PermissionGetResponse>>> getPermissionDetails(@RequestParam List<Long> ids) {
		List<PermissionGetResponse> permissions = permissionService.getPermissions(ids);
		return ResponseEntity.ok(ApiResponse.ok(permissions));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> updatePermission(@PathVariable Long id,
		@RequestBody @Valid PermissionUpdateRequest request) {
		permissionService.updatePermission(id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
		permissionService.deletePermission(id);
		return ResponseEntity.noContent().build();
	}
}
