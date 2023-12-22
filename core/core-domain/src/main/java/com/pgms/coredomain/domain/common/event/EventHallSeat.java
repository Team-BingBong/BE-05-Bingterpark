package com.pgms.coredomain.domain.common.event;

import jakarta.persistence.*;

@Entity
@Table(name = "event_hall_seat")
public class EventHallSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

}
