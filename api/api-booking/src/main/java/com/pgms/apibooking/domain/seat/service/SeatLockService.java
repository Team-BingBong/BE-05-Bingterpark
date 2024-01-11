package com.pgms.apibooking.domain.seat.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatLockService {

	private final static String SEAT_LOCK_CACHE_KEY_PREFIX = "seatId:";
	private final static String SEAT_LOCK_CACHE_VALUE_PREFIX = "memberId:";
	private final static int SEAT_LOCK_CACHE_EXPIRE_SECONDS = 420;

	private final RedisTemplate<String, String> redisTemplate;

	public Long getSelectorId(Long seatId) {
		String key = generateSeatLockKey(seatId);
		String value = redisTemplate.opsForValue().get(key);
		return value == null ? null : extractMemberId(value);
	}

	public boolean isSeatLocked(Long seatId) {
		return redisTemplate.opsForValue().get(generateSeatLockKey(seatId)) != null;
	}

	public void lockSeat(Long seatId, Long memberId) {
		String key = generateSeatLockKey(seatId);
		String value = generateSeatLockValue(memberId);
		Duration timeout = Duration.ofSeconds(SEAT_LOCK_CACHE_EXPIRE_SECONDS);
		redisTemplate.opsForValue().setIfAbsent(key, value, timeout);
	}

	public void unlockSeat(Long seatId) {
		redisTemplate.delete(generateSeatLockKey(seatId));
	}

	private String generateSeatLockKey(Long seatId) {
		return SEAT_LOCK_CACHE_KEY_PREFIX + seatId;
	}

	private String generateSeatLockValue(Long memberId) {
		return SEAT_LOCK_CACHE_VALUE_PREFIX + memberId;
	}

	private Long extractMemberId(String value) {
		return Long.parseLong(value.replace(SEAT_LOCK_CACHE_VALUE_PREFIX, ""));
	}
}
