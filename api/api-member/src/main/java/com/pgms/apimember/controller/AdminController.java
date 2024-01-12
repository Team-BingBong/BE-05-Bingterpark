package com.pgms.apimember.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apimember.dto.request.PageCondition;
import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.dto.response.MemberSummaryGetResponse;
import com.pgms.apimember.service.AdminService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.security.resolver.CurrentAccount;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자", description = "관리자 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

	@Operation(summary = "회원 목록 조회", description = "회원 목록 조회 API 입니다.")
	@GetMapping("/members")
	public ResponseEntity<ApiResponse<List<MemberSummaryGetResponse>>> getMembers(
		@ModelAttribute @Valid PageCondition pageCondition) {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getMembers(pageCondition)));
	}

	@Operation(summary = "회원 상세 조회", description = "회원 상세 조회 API 입니다.")
	@GetMapping("/members/details")
	public ResponseEntity<ApiResponse<List<MemberDetailGetResponse>>> getMemberDetails(
		@RequestParam List<Long> memberIds) {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getMemberDetails(memberIds)));
	}

	@Operation(summary = "관리자 본인 정보 조회", description = "관리자 본인 정보 조회 API 입니다.")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<AdminGetResponse>> getMyInfo(@CurrentAccount Long adminId) {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getAdmin(adminId)));
	}

	@Operation(summary = "관리자 본인 삭제", description = "관리자 본인 삭제 API 입니다.")
	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteMyAccount(@CurrentAccount Long adminId) {
		adminService.deleteAdmins(Collections.singletonList(adminId));
		return ResponseEntity.noContent().build();
	}
}
