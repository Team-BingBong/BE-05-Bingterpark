package com.pgms.apievent.exception;

import org.springframework.http.HttpStatus;

import com.pgms.coredomain.domain.common.BaseErrorCode;
import com.pgms.coredomain.response.ErrorResponse;

import lombok.Getter;

@Getter
public enum EventErrorCode implements BaseErrorCode {

	EVENT_NOT_FOUND("NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 공연입니다."),
	EVENT_HALL_NOT_FOUND("EVENT HALL NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 공연장입니다."),
	EVENT_SEAT_AREA_NOT_FOUND("EVENT SEAT AREA NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 공연 좌석 등급입니다."),
	ALREADY_EXIST_EVENT("DUPLICATE EVENT", HttpStatus.CONFLICT, "이미 존재하는 공연입니다."),
	EVENT_TIME_NOT_FOUND("EVENT TIME NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 회차입니다."),
	ALREADY_EXIST_EVENT_TIME("EVENT TIME ALREADY EXISTS", HttpStatus.CONFLICT, "공연에 대한 회차가 이미 존재합니다."),
	ALREADY_EXIST_EVENT_PLAY_TIME("EVENT PLAY TIME ALREADY EXISTS", HttpStatus.CONFLICT, "공연 회차별 공연 시간은 달라야합니다."),
	VALIDATION_FAILED("VALIDATION FAILED", HttpStatus.BAD_REQUEST, "입력값에 대한 검증에 실패했습니다."),
	REVIEWER_MISMATCH_EXCEPTION("REVIEWER MISMATCH", HttpStatus.BAD_REQUEST, "리뷰 작성자가 일치하지 않습니다."),
	EVENT_REVIEW_NOT_FOUND("EVENT REVIEW NOT FOUND", HttpStatus.NOT_FOUND, "존재하지 않는 공연 리뷰입니다."),
	UNSUPPORTED_FILE_EXTENSION("UNSUPPORTED FILE EXTENSION", HttpStatus.BAD_REQUEST, "지원되지 않는 파일 확장자입니다."),
	S3_UPLOAD_FAILED_EXCEPTION("S3 UPLOAD FAILED", HttpStatus.INTERNAL_SERVER_ERROR, "S3에 파일 업로드를 실패했습니다."),
	BINDING_FAILED_EXCEPTION("BINDING FAILED", HttpStatus.BAD_REQUEST, "ModelAttribute 필드 바인딩이 실패했습니다.");

	private final String errorCode;
	private final HttpStatus status;
	private final String message;

	EventErrorCode(String errorCode, HttpStatus status, String message) {
		this.errorCode = errorCode;
		this.status = status;
		this.message = message;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return new ErrorResponse(errorCode, message);
	}
}
