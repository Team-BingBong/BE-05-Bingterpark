package com.pgms.coredomain.domain.event;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SeatAreaType {
	R("R석"),
	S("S석");

	private final String description;
}
