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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_hall_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EventHall eventHall;

    public void setEventHall(EventHall eventHall){
        this.eventHall = eventHall;
    }

    public EventHallSeat(String name) {
        this.name = name;
    }
}
