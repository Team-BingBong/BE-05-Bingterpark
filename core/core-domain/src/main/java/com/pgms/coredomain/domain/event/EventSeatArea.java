package com.pgms.coredomain.domain.event;

import java.util.ArrayList;
import java.util.List;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

	@OneToMany(mappedBy = "eventSeatArea")
	private List<EventSeat> eventSeats = new ArrayList<>();

	public int getAvailableSeatCount() {
		return eventSeats.stream()
			.filter(EventSeat::isAvailable)
			.toList()
			.size();
	}
}
