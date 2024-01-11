package com.pgms.coredomain.domain.member.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "emailVerifyCode", timeToLive = 30 * 5) // 5분
public class EmailVerifyCode {
	@Id
	private String code;
	private String email;
}
