package com.pgms.apibooking.jwt;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Payload;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtPayload implements Payload {

	private final Long memberId;

	public Map<String, Long> toMap() {
		return new HashMap<>() {{
			put("memberId", memberId);
		}};
	}
}
