package com.pgms.coredomain.domain.event;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
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

	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Column(name = "endDate")
	private LocalDateTime endDate;

	@Column(name = "rating")
	private String rating;

	@Enumerated(value = EnumType.STRING)
	private Genre genre;

	@Lob
	@Column(name = "thumbnail")
	private String thumbnail;

	public Event(
		String title,
		String description,
		int runningTime,
		LocalDateTime startDate,
		LocalDateTime endDate,
		String rating,
		Genre genre,
		String thumbnail) {
		this.title = title;
		this.description = description;
		this.runningTime = runningTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.rating = rating;
		this.genre = genre;
		this.thumbnail = thumbnail;
	}
}
