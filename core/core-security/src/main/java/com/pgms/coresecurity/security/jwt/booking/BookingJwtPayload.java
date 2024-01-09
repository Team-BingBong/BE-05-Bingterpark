package com.pgms.coresecurity.security.jwt.booking;

import java.util.HashMap;
import java.util.Map;

public record BookingJwtPayload(String sessionId) {

	public Map<String, String> toMap() {
		return new HashMap<>() {{
			put("sessionId", sessionId);
		}};
	}
}
