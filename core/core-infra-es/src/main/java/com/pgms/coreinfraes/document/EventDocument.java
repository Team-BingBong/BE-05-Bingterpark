package com.pgms.coreinfraes.document;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.GenreType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_second;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(indexName = "event")
@Mapping(mappingPath = "es/event-mapping.json")
@Setting(settingPath = "es/event-setting.json")
public class EventDocument {

	@Id
	private Long id;

	private String title;

	private String description;

	@Field(type = FieldType.Date, format = {date_hour_minute_second, epoch_second})
	private LocalDateTime startedAt;

	@Field(type = FieldType.Date, format = {date_hour_minute_second, epoch_second})
	private LocalDateTime endedAt;

	private String viewRating;

	private GenreType genreType;

	private Double averageScore = 0.0;

	private Long eventHallId;

	public static EventDocument from(Event event) {
		return EventDocument.builder()
			.id(event.getId())
			.title(event.getTitle())
			.description(event.getDescription())
			.startedAt(event.getStartedAt())
			.endedAt(event.getEndedAt())
			.viewRating(event.getViewRating())
			.genreType(event.getGenreType())
			.averageScore(event.getAverageScore())
			.eventHallId(event.getEventHall().getId())
			.build();
	}
}
