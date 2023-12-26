package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_hall_seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventHallSeat extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "eventhall_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private EventHall eventHall;

    public EventHallSeat(String name) {
        this.name = name;
    }

    public void setEventHall(EventHall eventHall) {
        this.eventHall = eventHall;
    }
}
