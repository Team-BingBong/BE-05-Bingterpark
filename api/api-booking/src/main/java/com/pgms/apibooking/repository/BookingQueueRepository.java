package com.pgms.apibooking.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingQueueRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public void add(Long timeId, Long memberId) {
		Long waitingNumber = redisTemplate.opsForValue().increment(generateWaitingNumberKey(timeId), 1);
		redisTemplate.opsForZSet().add(String.valueOf(timeId), String.valueOf(memberId), waitingNumber);
	}
	
	public Long getRank(Long timeId, Long memberId) {
		return redisTemplate.opsForZSet().rank(String.valueOf(timeId), String.valueOf(memberId));
	}
	
	public Long getEntryLimit() {
		return 100L; //TODO: 캐시에서 가져오기
	}

	public void remove(Long timeId, Long memberId) {
		redisTemplate.opsForZSet().remove(String.valueOf(timeId), String.valueOf(memberId));
	}

	private String generateWaitingNumberKey(Long timeId) {
		return "waitingNumber:" + timeId;
	}
}