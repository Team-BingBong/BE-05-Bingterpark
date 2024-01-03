package com.pgms.apibooking.dto.response;

public record TokenIssueResponse(String token) {

	public static TokenIssueResponse from(String token) {
		return new TokenIssueResponse(token);
	}
}
