package com.pgms.coredomain.domain.event;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EventSeatAreaEdit {

    private SeatAreaType seatAreaType;

    private int price;

    @Builder
    public EventSeatAreaEdit(SeatAreaType seatAreaType, int price) {
        this.seatAreaType = seatAreaType;
        this.price = price;
    }
}
