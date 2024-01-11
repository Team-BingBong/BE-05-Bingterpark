package com.pgms.apimember.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apimember.dto.request.ConfirmRestoreRequest;
import com.pgms.apimember.dto.request.MemberInfoUpdateRequest;
import com.pgms.apimember.dto.request.MemberPasswordUpdateRequest;
import com.pgms.apimember.dto.request.MemberPasswordVerifyRequest;
import com.pgms.apimember.dto.request.MemberRestoreRequest;
import com.pgms.apimember.dto.request.MemberSignUpRequest;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.service.MemberService;
import com.pgms.coredomain.response.ApiResponse;
import com.pgms.coresecurity.security.resolver.CurrentAccount;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Long>> signUp(@RequestBody @Valid MemberSignUpRequest requestDto) {
		return ResponseEntity.ok(ApiResponse.ok(memberService.signUp(requestDto)));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<MemberDetailGetResponse>> getMyInfo(@CurrentAccount Long memberId) {
		return ResponseEntity.ok(ApiResponse.ok(memberService.getMemberDetail(memberId)));
	}

	@PatchMapping("/me")
	public ResponseEntity<Void> updateMyInfo(
		@CurrentAccount Long memberId,
		@RequestBody @Valid MemberInfoUpdateRequest requestDto) {
		memberService.updateMember(memberId, requestDto);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/me/password")
	public ResponseEntity<Void> updatePassword(
		@CurrentAccount Long memberId,
		@RequestBody @Valid MemberPasswordUpdateRequest requestDto) {
		memberService.updatePassword(memberId, requestDto);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/me/verify-password")
	public ResponseEntity<Void> verifyPassword(@CurrentAccount Long memberId,
		@RequestBody MemberPasswordVerifyRequest requestDto) {
		memberService.verifyPassword(memberId, requestDto.password());
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteMyAccount(@CurrentAccount Long memberId) {
		memberService.deleteMember(memberId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("send-restore-email")
	public ResponseEntity<ApiResponse<String>> sendRestoreEmail(@RequestBody @Valid MemberRestoreRequest requestDto) {
		memberService.sendRestoreEmail(requestDto);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/confirm-restore")
	public ResponseEntity<ApiResponse<Void>> confirmRestore(@RequestBody @Valid ConfirmRestoreRequest requestDto) {
		memberService.confirmRestoreMember(requestDto);
		return ResponseEntity.noContent().build();
	}
}
