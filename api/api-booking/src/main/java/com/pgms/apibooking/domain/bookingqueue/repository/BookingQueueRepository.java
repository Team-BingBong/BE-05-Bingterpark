package com.pgms.apibooking.domain.bookingqueue.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingQueueRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public void add(Long eventId, String sessionId) {
		double currentTimeSeconds = System.currentTimeMillis() / 1000.0;
		redisTemplate.opsForZSet().add(String.valueOf(eventId), sessionId, currentTimeSeconds);

		double timeLimitSeconds = currentTimeSeconds - (7 * 60);
		redisTemplate.opsForZSet().removeRangeByScore(String.valueOf(eventId), 0, timeLimitSeconds);
	}
	
	public Long getRank(Long eventId, String sessionId) {
		return redisTemplate.opsForZSet().rank(String.valueOf(eventId), sessionId);
	}
	
	public Long getEntryLimit() {
		return 500L;
	}

	public void remove(Long eventId, String sessionId) {
		redisTemplate.opsForZSet().remove(String.valueOf(eventId), sessionId);
	}
}
