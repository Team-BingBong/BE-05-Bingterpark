package com.pgms.apibooking.domain.bookingqueue.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.pgms.apibooking.common.util.RedisOperator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingQueueManager {

	private static final String WAITING_QUEUE_KEY_PREFIX = "waiting:eventId:";
	private static final String PARTICIPANT_QUEUE_KEY_PREFIX = "participant:eventId:";

	private final RedisOperator redisOperator;

	public void addToWaitingQueue(Long eventId, String sessionId, double currentTimeSeconds) {
		String key = generateWaitingQueueKey(eventId);
		redisOperator.addToZSet(key, sessionId, currentTimeSeconds);
	}

	public Optional<Long> getRankInWaitingQueue(Long eventId, String sessionId) {
		String key = generateWaitingQueueKey(eventId);
		Long rank = redisOperator.getRankFromZSet(key, sessionId);
		return Optional.ofNullable(rank);
	}

	public void removeFromWaitingQueue(Long eventId, String sessionId) {
		String key = generateWaitingQueueKey(eventId);
		redisOperator.removeElementFromZSet(key, sessionId);
	}

	public void removeRangeByScoreFromWaitingQueue(Long eventId, double minScore, double maxScore) {
		String key = generateWaitingQueueKey(eventId);
		redisOperator.removeRangeByScoreFromZSet(key, minScore, maxScore);
	}

	public void addToParticipantQueue(Long eventId, String sessionId, double currentTimeSeconds) {
		String key = generateParticipantQueueKey(eventId);
		redisOperator.addToZSet(key, sessionId, currentTimeSeconds);
	}

	public Long getSizeOfParticipantQueue(Long eventId) {
		String key = generateParticipantQueueKey(eventId);
		return redisOperator.getSizeOfZSet(key);
	}

	public Double getElementScore(Long eventId, String sessionId) {
		String key = generateParticipantQueueKey(eventId);
		return redisOperator.getScoreFromZSet(key, sessionId);
	}

	public void removeFromParticipantQueue(Long eventId, String sessionId) {
		String key = generateParticipantQueueKey(eventId);
		redisOperator.removeElementFromZSet(key, sessionId);
	}

	public void removeRangeByScoreFromParticipantQueue(Long eventId, double minScore, double maxScore) {
		String key = generateParticipantQueueKey(eventId);
		redisOperator.removeRangeByScoreFromZSet(key, minScore, maxScore);
	}

	private String generateWaitingQueueKey(Long eventId) {
		return WAITING_QUEUE_KEY_PREFIX + eventId;
	}

	private String generateParticipantQueueKey(Long eventId) {
		return PARTICIPANT_QUEUE_KEY_PREFIX + eventId;
	}
}
