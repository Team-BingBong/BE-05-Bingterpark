package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_seat")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventSeat extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private EventSeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_time_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EventTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_seat_area_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EventSeatArea eventSeatArea;

    public boolean isAvailable() {
        return status == EventSeatStatus.AVAILABLE;
    }

    public boolean isBooked() {
        return status == EventSeatStatus.BOOKED;
    }

    public void updateStatus(EventSeatStatus status) {
        this.status = status;
    }

    @Builder
    public EventSeat(String name, EventSeatStatus status, EventTime eventTime, EventSeatArea eventSeatArea) {
        this.name = name;
        this.status = status;
        this.eventTime = eventTime;
        this.eventSeatArea = eventSeatArea;
    }
}
