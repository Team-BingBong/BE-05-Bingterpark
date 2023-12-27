package com.pgms.coredomain.domain.event;

import com.pgms.coredomain.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "event_seat_area")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventSeatArea extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "area_type")
	@Enumerated(value = EnumType.STRING)
	private SeatAreaType seatAreaType;

	@Column(name = "price")
	private int price;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Event event;

	public EventSeatArea(SeatAreaType seatAreaType, int price, Event event) {
		this.seatAreaType = seatAreaType;
		this.price = price;
		this.event = event;
	}

	public void updateEventSeatAreaPriceAndType(SeatAreaType seatAreaType, int price){
		this.seatAreaType = seatAreaType;
		this.price = price;
	}
}
