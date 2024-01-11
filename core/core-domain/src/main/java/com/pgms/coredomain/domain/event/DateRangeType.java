package com.pgms.coredomain.domain.event;

public enum DateRangeType {
    DAILY(1),
    WEEKLY(7);

    private final int dateRange;

    DateRangeType(int dateRange) {
        this.dateRange = dateRange;
    }

    public static GenreType of(String input) {
        try {
            return GenreType.valueOf(input.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않는 날짜 범위 입니다. : " + input);
        }
    }
}
