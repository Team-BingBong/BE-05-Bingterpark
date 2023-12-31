package com.pgms.apimember.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pgms.apimember.dto.request.MemberPasswordUpdateRequest;
import com.pgms.apimember.dto.request.MemberPasswordVerifyRequest;
import com.pgms.apimember.dto.response.MemberDetailGetResponse;
import com.pgms.apimember.service.MemberService;
import com.pgms.coredomain.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<MemberDetailGetResponse>> getMyInfo(Long memberId) {
		// TODO: 현재 로그인한 사용자의 ID를 가져오는 어노테이션 필요 (security)
		return ResponseEntity.ok(ApiResponse.ok(memberService.getMemberDetail(memberId)));
	}

	@PatchMapping("/me/password")
	public ResponseEntity<Void> updatePassword(
		Long memberId,
		@RequestBody @Valid MemberPasswordUpdateRequest requestDto) {
		memberService.updatePassword(memberId, requestDto);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/me/verify-password")
	public ResponseEntity<Void> verifyPassword(Long memberId, @RequestBody MemberPasswordVerifyRequest requestDto) {
		memberService.verifyPassword(memberId, requestDto.password());
		return ResponseEntity.noContent().build();
	}

}
