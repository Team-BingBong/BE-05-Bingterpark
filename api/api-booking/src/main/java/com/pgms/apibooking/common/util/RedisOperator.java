package com.pgms.apibooking.common.util;

import java.time.Duration;
import java.util.function.Supplier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.pgms.apibooking.common.exception.BookingException;
import com.pgms.coredomain.domain.common.BookingErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisOperator {

	private final RedisTemplate<String, String> redisTemplate;

	public void setIfAbsent(String key, String value, Integer expirationSeconds) {
		Duration timeout = Duration.ofSeconds(expirationSeconds);
		tryOperation(() -> redisTemplate.opsForValue().setIfAbsent(key, value, timeout));
	}

	public String get(String key) {
		return tryOperation(() -> redisTemplate.opsForValue().get(key));
	}

	public void delete(String key) {
		tryOperation(() -> redisTemplate.delete(key));
	}

	public void addToZSet(String key, String value, double score) {
		tryOperation(() -> redisTemplate.opsForZSet().add(key, value, score));
	}

	public Long getSizeOfZSet(String key) {
		return tryOperation(() -> redisTemplate.opsForZSet().size(key));
	}

	public Long getRankFromZSet(String key, String value) {
		return tryOperation(() -> redisTemplate.opsForZSet().rank(key, value));
	}

	public Double getScoreFromZSet(String key, String index) {
		return tryOperation(() -> redisTemplate.opsForZSet().score(key, index));
	}

	public void removeElementFromZSet(String key, String value) {
		tryOperation(() -> redisTemplate.opsForZSet().remove(key, value));
	}

	public void removeRangeByScoreFromZSet(String key, double minScore, double maxScore) {
		tryOperation(() -> redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore));
	}

	private <T> T tryOperation(Supplier<T> operation) {
		try {
			return operation.get();
		} catch (Exception e) {
			log.error("RedisManager occurred: " + e.getMessage(), e);
			throw new BookingException(BookingErrorCode.SERVICE_UNAVAILABLE);
		}
	}
}
