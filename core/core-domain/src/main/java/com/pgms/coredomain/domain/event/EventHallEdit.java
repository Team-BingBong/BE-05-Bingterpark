package com.pgms.coredomain.domain.event;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EventHallEdit {

    private String name;

    private String address;

    private List<EventHallSeat> eventHallSeats = new ArrayList<>();

    @Builder
    public EventHallEdit(String name, String address, List<EventHallSeat> eventHallSeats) {
        this.name = name;
        this.address = address;
        this.eventHallSeats = eventHallSeats;
    }
}
