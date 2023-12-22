package com.pgms.coredomain.domain.event;

import java.time.LocalDateTime;

import com.pgms.coredomain.domain.common.BaseEntity;

import jakarta.persistence.*;
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

	@Column(name = "start_date")
	private LocalDateTime startDate;

	@Column(name = "endDate")
	private LocalDateTime endDate;

	@Column(name = "rating")
	private String rating;

	@Column(name = "genre")
	@Enumerated(value = EnumType.STRING)
	private GenreType genreType;

	@Lob
	@Column(name = "thumbnail")
	private String thumbnail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_hall_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private EventHall eventHall;
}
