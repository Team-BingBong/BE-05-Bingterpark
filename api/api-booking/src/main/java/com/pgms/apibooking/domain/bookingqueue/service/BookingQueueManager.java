package com.pgms.apibooking.domain.bookingqueue.service;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingQueueManager {

	private final RedisTemplate<String, String> redisTemplate;

	public void add(Long eventId, String sessionId, double currentTimeSeconds) {
		redisTemplate.opsForZSet().add(String.valueOf(eventId), sessionId, currentTimeSeconds);
	}
	
	public Optional<Long> getRank(Long eventId, String sessionId) {
		return Optional.ofNullable(redisTemplate.opsForZSet().rank(String.valueOf(eventId), sessionId));
	}
	
	public Long getEntryLimit() {
		return 500L;
	}

	public void remove(Long eventId, String sessionId) {
		redisTemplate.opsForZSet().remove(String.valueOf(eventId), sessionId);
	}

	public void removeRangeByScore(Long eventId, double min, double max) {
		redisTemplate.opsForZSet().removeRangeByScore(String.valueOf(eventId), min, max);
	}
}
