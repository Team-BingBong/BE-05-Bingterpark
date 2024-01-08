package com.pgms.apibooking.domain.bookingqueue.dto.response;

public record SessionIdIssueResponse(String sessionId) {

	public static SessionIdIssueResponse from(String sessionId) {
		return new SessionIdIssueResponse(sessionId);
	}
}
