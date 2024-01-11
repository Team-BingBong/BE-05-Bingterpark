package com.pgms.coredomain.domain.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.pgms.coredomain.domain.booking.Booking;
import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "event_time")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventTime extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "round")
	private int round;

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	@Column(name = "ended_at")
	private LocalDateTime endedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Event event;

	@OneToMany(mappedBy = "time")
	private List<Booking> bookings = new ArrayList<>();

	@Builder
	public EventTime(int round, LocalDateTime startedAt, LocalDateTime endedAt, Event event) {
		validateEventTimePlayTime(startedAt, endedAt);
		this.round = round;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.event = event;
	}

	public void updateEventTime(LocalDateTime startedAt, LocalDateTime endedAt) {
		validateEventTimePlayTime(startedAt, endedAt);
		this.startedAt = startedAt;
		this.endedAt = endedAt;
	}

	private void validateEventTimePlayTime(LocalDateTime startedAt, LocalDateTime endedAt) {
		if (startedAt.isAfter(endedAt)) {
			throw new IllegalArgumentException("StartedAt should be before or equal to EndedAt");
		}
	}
}
