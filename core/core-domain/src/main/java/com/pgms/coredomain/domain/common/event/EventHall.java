package com.pgms.coredomain.domain.common.event;

import jakarta.persistence.*;

@Entity
@Table(name = "event_hall")
public class EventHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;
}
