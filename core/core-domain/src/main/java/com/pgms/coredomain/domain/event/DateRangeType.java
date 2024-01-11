package com.pgms.coredomain.domain.event;

import lombok.Getter;

@Getter
public enum DateRangeType {
    DAILY(1),
    WEEKLY(7);

    private final long dateRange;

    DateRangeType(int dateRange) {
        this.dateRange = dateRange;
    }

    public static DateRangeType of(String input) {
        try {
            return DateRangeType.valueOf(input.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않는 날짜 범위 입니다. : " + input);
        }
    }
}
