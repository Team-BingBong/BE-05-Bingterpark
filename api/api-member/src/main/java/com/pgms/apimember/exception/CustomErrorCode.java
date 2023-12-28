package com.pgms.apimember.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum CustomErrorCode {
	// ADMIN
	ADMIN_NOT_FOUND("NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다."),
	DUPLICATED_ADMIN_EMAIL("DUPLICATED ADMIN EMAIL", HttpStatus.BAD_REQUEST, "이미 존재하는 관리자 이메일입니다."),
	NOT_AUTHORIZED("NOT AUTHORIZED", HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
	ADMIN_ROLE_NOT_FOUND("ADMIN ROLE NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 관리자 역할입니다."),
	DUPLICATED_ROLE("DUPLICATED ROLE", HttpStatus.BAD_REQUEST, "이미 존재하는 관리자 역할입니다."),
	ROLE_IN_USE("ROLE IN USE", HttpStatus.BAD_REQUEST, "사용중인 역할입니다."),
	ADMIN_PERMISSION_NOT_FOUND("ADMIN PERMISSION NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 관리자 권한입니다."),
	DUPLICATED_PERMISSION("DUPLICATED PERMISSION", HttpStatus.BAD_REQUEST, "이미 존재하는 권한입니다"),
	PERMISSION_ASSIGNED("PERMISSION ASSIGNED", HttpStatus.BAD_REQUEST, "이미 할당된 권한은 삭제할 수 없습니다."),
	ROLE_PERMISSION_NOT_FOUND("ROLE PERMISSION NOT FOUND", HttpStatus.BAD_REQUEST, "존재하지 않는 역할 권한입니다."),

	// MEMBER
	MEMBER_NOT_FOUND("NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	DUPLICATED_MEMBER_EMAIL("DUPLICATED MEMBER EMAIL", HttpStatus.BAD_REQUEST, "이미 존재하는 회원 이메일입니다."),
	NOT_MATCH_PASSWORD("NOT MATCH PASSWORD", HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	NOT_MATCH_PASSWORD_CONFIRM("NOT MATCH PASSWORD CONFIRM", HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다."),
	VALIDATION_FAILED("VALIDATION FAILED", HttpStatus.BAD_REQUEST, "입력값에 대한 검증에 실패했습니다.");

	private final String errorCode;
	private final HttpStatus status;
	private final String message;

	CustomErrorCode(String errorCode, HttpStatus status, String message) {
		this.errorCode = errorCode;
		this.status = status;
		this.message = message;
	}
}
