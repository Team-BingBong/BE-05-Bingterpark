package com.pgms.apibooking.domain.seat.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.pgms.apibooking.common.util.RedisOperator;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SeatLockManager {

	private final static String SEAT_LOCK_CACHE_KEY_PREFIX = "seatId:";
	private final static String SEAT_LOCK_CACHE_VALUE_PREFIX = "tokenSessionId:";

	private final RedisOperator redisOperator;

	public Optional<String> getSelectorId(Long seatId) {
		String key = generateSeatLockKey(seatId);
		String value = redisOperator.get(key);
		return Optional.ofNullable(value == null ? null : extractSessionId(value));
	}

	public void lockSeat(Long seatId, String tokenSessionId, Integer expirationSeconds) {
		String key = generateSeatLockKey(seatId);
		String value = generateSeatLockValue(tokenSessionId);
		redisOperator.setIfAbsent(key, value, expirationSeconds);
	}

	public void unlockSeat(Long seatId) {
		redisOperator.delete(generateSeatLockKey(seatId));
	}

	private String generateSeatLockKey(Long seatId) {
		return SEAT_LOCK_CACHE_KEY_PREFIX + seatId;
	}

	private String generateSeatLockValue(String tokenSessionId) {
		return SEAT_LOCK_CACHE_VALUE_PREFIX + tokenSessionId;
	}

	private String extractSessionId(String value) {
		return value.replace(SEAT_LOCK_CACHE_VALUE_PREFIX, "");
	}
}
