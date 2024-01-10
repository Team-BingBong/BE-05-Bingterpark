package com.pgms.apibooking.domain.bookingqueue.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.coredomain.domain.common.BookingErrorCode;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingQueueRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public void add(Long eventId, String sessionId, double currentTimeSeconds) {
		redisTemplate.opsForZSet().add(String.valueOf(eventId), sessionId, currentTimeSeconds);
	}
	
	public Long getRank(Long eventId, String sessionId) {
		Long rank = redisTemplate.opsForZSet().rank(String.valueOf(eventId), sessionId);
		if (rank == null) {
			throw new BookingException(BookingErrorCode.NOT_IN_QUEUE);
		}
		return rank;
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
