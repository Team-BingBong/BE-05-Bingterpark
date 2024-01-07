package com.pgms.coredomain.domain.event;

import lombok.Getter;

@Getter
public enum GenreType {
	MUSICAL("뮤지컬"),
	CONCERT("콘서트"),
	PLAY("연극"),
	CLASSIC("클래식"),
	SPORTS("스포츠"),
	CAMPING("캠핑");

	private final String description;

	GenreType(String description) {
		this.description = description;
	}

	public static GenreType of(String input) {
		try {
			return GenreType.valueOf(input);
		} catch (Exception e) {
			throw e;
		}
	}
}
