package com.pgms.coredomain.domain.common;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum MemberErrorCode implements BaseErrorCode {
	// ADMIN
	ADMIN_NOT_FOUND("NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다."),
	DUPLICATED_ADMIN_EMAIL("DUPLICATED ADMIN EMAIL", HttpStatus.BAD_REQUEST, "이미 존재하는 관리자 이메일입니다."),
	NOT_ACTIVE_ADMIN("NOT ACTIVE ADMIN", HttpStatus.BAD_REQUEST, "활성화되지 않은 관리자입니다. 슈퍼 관리자에게 문의해주세요."),
	ADMIN_ALREADY_DELETED("ADMIN ALREADY DELETED", HttpStatus.BAD_REQUEST, "탈퇴한 관리자입니다. 슈퍼 관리자에게 문의해주세요."),

	// MEMBER
	MEMBER_NOT_FOUND("NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	DUPLICATED_MEMBER_EMAIL("DUPLICATED MEMBER EMAIL", HttpStatus.BAD_REQUEST, "이미 존재하는 회원 이메일입니다."),
	MEMBER_ALREADY_DELETED("MEMBER ALREADY DELETED", HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다. 사용을 원하시면 회원 복구를 해주세요."),
	PASSWORD_NOT_MATCHED("PASSWORD NOT MATCHED", HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
	PASSWORD_CONFIRM_NOT_MATCHED("PASSWORD CONFIRM NOT MATCHED", HttpStatus.BAD_REQUEST, "비밀번호 확인이 일치하지 않습니다."),
	NOT_ALLOWED_BY_PROVIDER("NOT ALLOWED BY PROVIDER", HttpStatus.BAD_REQUEST, "소셜 로그인 회원은 불가능한 요청입니다."),
	EMAIL_SEND_FAIL("EMAIL SEND FAIL", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 발송에 실패했습니다."),
	EMAIL_SEND_FAILED("EMAIL SEND FAIL", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 발송에 실패했습니다."),
	CREATE_VERIFY_CODE_FAILED("CREATE VERIFY CODE FAIL", HttpStatus.INTERNAL_SERVER_ERROR, "이메일 인증 코드 생성에 실패했습니다."),
	EMAIL_VERIFY_FAILED("EMAIL VERIFY FAIL", HttpStatus.BAD_REQUEST, "이메일 인증에 실패했습니다.");

	private final String errorCode;
	private final HttpStatus status;
	private final String message;

	MemberErrorCode(String errorCode, HttpStatus status, String message) {
		this.errorCode = errorCode;
		this.status = status;
		this.message = message;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(errorCode, message);
	}
}
