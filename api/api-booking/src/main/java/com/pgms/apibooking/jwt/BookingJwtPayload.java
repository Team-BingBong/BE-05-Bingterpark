package com.pgms.apibooking.jwt;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingJwtPayload implements Payload {

	private final String sessionId;

	public Map<String, String> toMap() {
		return new HashMap<>() {{
			put("sessionId", sessionId);
		}};
	}
}
