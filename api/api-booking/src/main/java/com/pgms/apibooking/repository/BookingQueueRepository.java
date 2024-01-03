package com.pgms.apibooking.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingQueueRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public void add(Long eventId, String sessionId) {
		Long waitingNumber = redisTemplate.opsForValue().increment(generateWaitingNumberKey(eventId), 1);
		redisTemplate.opsForZSet().add(String.valueOf(eventId), sessionId, waitingNumber);
	}
	
	public Long getRank(Long eventId, String sessionId) {
		return redisTemplate.opsForZSet().rank(String.valueOf(eventId), sessionId);
	}
	
	public Long getEntryLimit() {
		return 100L;
	}

	public void remove(Long eventId, String sessionId) {
		redisTemplate.opsForZSet().remove(String.valueOf(eventId), sessionId);
	}

	private String generateWaitingNumberKey(Long eventId) {
		return "waitingNumber:" + eventId;
	}
}
