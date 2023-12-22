package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "event_seat")
public class EventSeat extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private EventSeatStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_time_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EventTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_seat_area_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EventSeatArea eventSeatArea;
}
