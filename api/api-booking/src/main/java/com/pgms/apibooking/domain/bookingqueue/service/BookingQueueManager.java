package com.pgms.apibooking.domain.bookingqueue.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.pgms.apibooking.common.util.RedisOperator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingQueueManager {

	private final RedisOperator redisOperator;

	public void add(Long eventId, String sessionId, double currentTimeSeconds) {
		redisOperator.addToZSet(String.valueOf(eventId), sessionId, currentTimeSeconds);
	}
	
	public Optional<Long> getRank(Long eventId, String sessionId) {
		Long rank = redisOperator.getRankFromZSet(String.valueOf(eventId), sessionId);
		return Optional.ofNullable(rank);
	}

	public void remove(Long eventId, String sessionId) {
		redisOperator.removeElementFromZSet(String.valueOf(eventId), sessionId);
	}

	public void removeRangeByScore(Long eventId, double minScore, double maxScore) {
		redisOperator.removeRangeByScoreFromZSet(String.valueOf(eventId), minScore, maxScore);
	}
}
