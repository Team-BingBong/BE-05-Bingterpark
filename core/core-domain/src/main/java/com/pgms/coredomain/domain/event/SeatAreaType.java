package com.pgms.coredomain.domain.event;

import lombok.Getter;

@Getter
public enum SeatAreaType {
	R("R석"),
	S("S석");

	private final String description;

	SeatAreaType(String description) {
		this.description = description;
	}

	public static SeatAreaType of(String input) {
		try {
			return SeatAreaType.valueOf(input.toUpperCase());
		} catch (Exception e) {
			throw new IllegalArgumentException("존재하지 않는 좌석 구역 등급입니다. : " + input);
		}
	}
}
