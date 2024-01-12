package com.pgms.coredomain.domain.event;

import java.util.ArrayList;
import java.util.List;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column(name = "name")
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
		setEventHallSeatsEventHall();
	}

	public void setEventHallSeatsEventHall() {
		if (this.eventHallSeats == null)
			return;
		this.eventHallSeats.forEach(eventHallSeat -> eventHallSeat.setEventHall(this));
	}

	public void updateEventHall(EventHallEdit eventHallEdit) {
		this.name = eventHallEdit.getName();
		this.address = eventHallEdit.getAddress();
		this.eventHallSeats = eventHallEdit.getEventHallSeats();
	}
}
