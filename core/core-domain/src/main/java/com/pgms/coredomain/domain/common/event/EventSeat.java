package com.pgms.coredomain.domain.common.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;
import org.yaml.snakeyaml.events.Event;

@Entity
@Table(name = "event_seat")
public class EventSeat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private EventStatus event;
}
