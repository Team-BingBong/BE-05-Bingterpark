package com.pgms.coredomain.domain.member.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "blockedToken", timeToLive = 30 * 60) // 30분(액세스 토큰 유효시간)
public class BlockedToken {
	@Id
	private String token;
}
