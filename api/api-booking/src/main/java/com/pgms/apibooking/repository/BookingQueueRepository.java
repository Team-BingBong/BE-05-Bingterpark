package com.pgms.apibooking.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingQueueRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public void add(Long timeId, Long memberId) {
		String key = "waitingNumber:" + timeId;
		Long waitingNumber = redisTemplate.opsForValue().increment(key, 1);
		redisTemplate.opsForZSet().add(String.valueOf(timeId), String.valueOf(memberId), waitingNumber);
	}
}
