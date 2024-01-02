package com.pgms.apibooking.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingQueueRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public void add(Long timeId, Long memberId) {
		String key = generateKey(timeId);
		Long waitingNumber = redisTemplate.opsForValue().increment(key, 1);
		redisTemplate.opsForZSet().add(String.valueOf(timeId), String.valueOf(memberId), waitingNumber);
	}

	public Long countRemainingQueue(Long timeId) {
		String key = generateKey(timeId);
		return redisTemplate.opsForZSet().rank(key, String.valueOf(1L));
	}

	public Boolean checkMyTurn(Long timeId, long memberId) {
		String key = generateKey(timeId);
		Long entryLimit = 100L; //TODO: 캐시에서 가져오기
		Long myWaitingNumber = redisTemplate.opsForZSet().rank(key, String.valueOf(memberId));
		return myWaitingNumber != null && myWaitingNumber < entryLimit;
	}

	private String generateKey(Long timeId) {
		return "waitingNumber:" + timeId;
	}
}
