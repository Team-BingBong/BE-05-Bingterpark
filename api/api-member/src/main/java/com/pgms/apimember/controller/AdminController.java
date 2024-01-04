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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;

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
	public ResponseEntity<ApiResponse<AdminGetResponse>> getMyInfo(@CurrentAccount Long adminId) {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getAdmin(adminId)));
	}

	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteMyAccount(@CurrentAccount Long adminId) {
		adminService.deleteAdmins(Collections.singletonList(adminId));
		return ResponseEntity.noContent().build();
	}
}
