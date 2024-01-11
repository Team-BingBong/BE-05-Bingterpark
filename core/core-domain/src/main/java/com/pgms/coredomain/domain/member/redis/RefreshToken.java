package com.pgms.coredomain.domain.member.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "token", timeToLive = 60 * 60 * 24 * 7) // 7일
public class RefreshToken {
	@Id
	private String refreshToken;
	private String accessToken;
	private String accountType; // admin, member -> TODO : enum으로 개선
	private String email;
}
