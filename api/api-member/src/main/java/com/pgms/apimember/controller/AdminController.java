package com.pgms.apimember.controller;

import java.net.URI;
import java.util.Collections;
import java.util.List;

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
import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.dto.response.MemberSummaryGetResponse;
import com.pgms.apimember.service.AdminService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

	private static final Long TEMP_CURRENT_ID = 1L;

	private final AdminService adminService;

	// 슈퍼 관리자 기능
	// TODO: 슈퍼 관리자인지 확인 필요 (security)
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

	// 일반 관리자 기능
	// TODO: 일반 관리자인지 확인 필요 (security)
	@GetMapping("/members")
	public ResponseEntity<ApiResponse<List<MemberSummaryGetResponse>>> getMembers(
		@ModelAttribute @Valid PageCondition pageCondition) {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getMembers(pageCondition)));
	}

	@GetMapping("/members/details")
	public ResponseEntity<ApiResponse<List<MemberDetailGetResponse>>> getMemberDetails(
		@RequestParam List<Long> memberIds) {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getMemberDetails(memberIds)));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<AdminGetResponse>> getMyInfo() {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getAdmin(TEMP_CURRENT_ID)));
	}

	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteMyAccount() {
		adminService.deleteAdmins(Collections.singletonList(TEMP_CURRENT_ID));
		return ResponseEntity.noContent().build();
	}
}
