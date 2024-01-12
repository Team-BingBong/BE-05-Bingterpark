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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원", description = "회원 관련 API 입니다.")
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "회원가입", description = "회원가입 API 입니다.")
	@PostMapping("/signup")
	public ResponseEntity<ApiResponse<Long>> signUp(@RequestBody @Valid MemberSignUpRequest requestDto) {
		return ResponseEntity.ok(ApiResponse.ok(memberService.signUp(requestDto)));
	}

	@Operation(summary = "회원 정보 조회", description = "회원 정보 조회 API 입니다.")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<MemberDetailGetResponse>> getMyInfo(@CurrentAccount Long memberId) {
		return ResponseEntity.ok(ApiResponse.ok(memberService.getMemberDetail(memberId)));
	}

	@Operation(summary = "회원 수정", description = "회원 수정 API 입니다.")
	@PatchMapping("/me")
	public ResponseEntity<Void> updateMyInfo(
		@CurrentAccount Long memberId,
		@RequestBody @Valid MemberInfoUpdateRequest requestDto) {
		memberService.updateMember(memberId, requestDto);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "비밀번호 수정", description = "비밀번호 수정 API 입니다.")
	@PatchMapping("/me/password")
	public ResponseEntity<Void> updatePassword(
		@CurrentAccount Long memberId,
		@RequestBody @Valid MemberPasswordUpdateRequest requestDto) {
		memberService.updatePassword(memberId, requestDto);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "비밀번호 확인", description = "비밀번호 확인 API 입니다.")
	@PostMapping("/me/verify-password")
	public ResponseEntity<Void> verifyPassword(@CurrentAccount Long memberId,
		@RequestBody MemberPasswordVerifyRequest requestDto) {
		memberService.verifyPassword(memberId, requestDto.password());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API 입니다.")
	@DeleteMapping("/me")
	public ResponseEntity<ApiResponse<Void>> deleteMyAccount(@CurrentAccount Long memberId) {
		memberService.deleteMember(memberId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "휴면 해제 메일 전송", description = "휴면 해제 메일 전송 API 입니다.")
	@PostMapping("/send-restore-email")
	public ResponseEntity<ApiResponse<String>> sendRestoreEmail(@RequestBody @Valid MemberRestoreRequest requestDto) {
		memberService.sendRestoreEmail(requestDto);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "휴면 해제 본인 인증", description = "휴면 해제 본인 인증 API 입니다.")
	@PatchMapping("/confirm-restore")
	public ResponseEntity<ApiResponse<Void>> confirmRestore(@RequestBody @Valid ConfirmRestoreRequest requestDto) {
		memberService.confirmRestoreMember(requestDto);
		return ResponseEntity.noContent().build();
	}
}
