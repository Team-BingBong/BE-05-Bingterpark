package com.pgms.coredomain.domain.event;

import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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
