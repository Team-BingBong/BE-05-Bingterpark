package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "event_hall")
public class EventHall extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name")
    private String name;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "eventHall", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventHallSeat> eventHallSeats = new ArrayList<>();

    @Builder
    public EventHall(String name, String address, List<EventHallSeat> eventHallSeats) {
        this.name = name;
        this.address = address;
        this.eventHallSeats = eventHallSeats;
    }

    public void updateEventHall(EventHallEdit eventHallEdit){
        this.name = eventHallEdit.getName();
        this.address = eventHallEdit.getAddress();
        this.eventHallSeats = eventHallEdit.getEventHallSeats();
    }
}
