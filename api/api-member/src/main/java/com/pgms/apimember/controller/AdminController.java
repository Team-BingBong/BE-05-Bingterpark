package com.pgms.apimember.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pgms.apimember.dto.request.AdminCreateRequest;
import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.dto.response.MemberSummaryGetResponse;
import com.pgms.apimember.service.AdminService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private static final Long TEMP_CURRENT_ID = 1L;

	private final AdminService adminService;

	// 슈퍼 관리자 기능
	@PostMapping
	public ResponseEntity<ApiResponse<Long>> createAdmin(
		@RequestBody @NotNull AdminCreateRequest requestDto) {
		final Long adminId = adminService.createAdmin(requestDto);
		final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
			.buildAndExpand(adminId)
			.toUri();
		return ResponseEntity.created(uri).body(ApiResponse.ok(adminId));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<AdminGetResponse>>> getAdmins() {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getAdmins()));
	}

	@PatchMapping
	public ResponseEntity<ApiResponse<Void>> updateAdmin() {
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping
	public ResponseEntity<ApiResponse<Void>> deleteAdmin() {
		return ResponseEntity.noContent().build();
	}

	//일반 관리자 기능
	@GetMapping("/members")
	public ResponseEntity<ApiResponse<List<MemberSummaryGetResponse>>> getMembers() {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getMembers()));
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
		adminService.deleteAdmin(TEMP_CURRENT_ID);
		return ResponseEntity.noContent().build();
	}
}
