package com.pgms.apibooking.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BookingQueueRepository {

	private final RedisTemplate<String, String> redisTemplate;

	public void add(Long eventId, Long memberId) {
		Long waitingNumber = redisTemplate.opsForValue().increment(generateWaitingNumberKey(eventId), 1);
		redisTemplate.opsForZSet().add(String.valueOf(eventId), String.valueOf(memberId), waitingNumber);
	}
	
	public Long getRank(Long eventId, Long memberId) {
		return redisTemplate.opsForZSet().rank(String.valueOf(eventId), String.valueOf(memberId));
	}
	
	public Long getEntryLimit() {
		return 100L; //TODO: 캐시에서 가져오기
	}

	public void remove(Long eventId, Long memberId) {
		redisTemplate.opsForZSet().remove(String.valueOf(eventId), String.valueOf(memberId));
	}

	private String generateWaitingNumberKey(Long eventId) {
		return "waitingNumber:" + eventId;
	}
}
