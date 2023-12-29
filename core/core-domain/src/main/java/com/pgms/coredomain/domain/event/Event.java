package com.pgms.coredomain.domain.event;

import java.time.LocalDateTime;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title")
	private String title;

	@Lob
	@Column(name = "description")
	private String description;

	@Column(name = "running_time")
	private int runningTime;

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	@Column(name = "ended_at")
	private LocalDateTime endedAt;

	@Column(name = "rating")
	private String rating;

	@Column(name = "genre")
	@Enumerated(value = EnumType.STRING)
	private GenreType genreType;

	@Column(name = "average_score")
	private Double averageScore = 0.0;

	@Lob
	@Column(name = "thumbnail")
	private String thumbnail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_hall_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private EventHall eventHall;

	@Builder
	public Event(
		String title,
		String description,
		int runningTime,
		LocalDateTime startedAt,
		LocalDateTime endedAt,
		String rating,
		GenreType genreType,
		String thumbnail,
		EventHall eventHall) {
		this.title = title;
		this.description = description;
		this.runningTime = runningTime;
		this.startedAt = startedAt;
		this.endedAt = endedAt;
		this.rating = rating;
		this.genreType = genreType;
		this.thumbnail = thumbnail;
		this.eventHall = eventHall;
	}

	public void updateEvent(EventEdit eventEdit) {
		this.title = eventEdit.title();
		this.description = eventEdit.description();
		this.runningTime = eventEdit.runningTime();
		this.startedAt = eventEdit.startDate();
		this.endedAt = eventEdit.endDate();
		this.rating = eventEdit.rating();
		this.genreType = eventEdit.genreType();
		this.eventHall = eventEdit.eventHall();
	}

	public void updateAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}

	public void updateThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}
