package com.pgms.apibooking.dto.response;

public record SessionIdIssueResponse(String sessionId) {

	public static SessionIdIssueResponse from(String sessionId) {
		return new SessionIdIssueResponse(sessionId);
	}
}
