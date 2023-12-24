package com.pgms.apimember.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apimember.dto.response.AdminGetResponse;
import com.pgms.apimember.service.AdminService;
import com.pgms.coredomain.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private static final Long TEMP_CURRENT_ID = 1L;

	private final AdminService adminService;

	@GetMapping("/members")
	public ResponseEntity<ApiResponse<?>> getMembers(@RequestParam(required = false) List<Long> memberIds) {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getMembers(memberIds)));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<AdminGetResponse>> getMyInfo() {
		return ResponseEntity.ok(ApiResponse.ok(adminService.getAdmin(TEMP_CURRENT_ID)));
	}

	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteMyAccount() {
		adminService.deleteAdmin(TEMP_CURRENT_ID);
		return ResponseEntity.ok(ApiResponse.ok(null));
	}
}
