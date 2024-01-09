package com.pgms.coreinfraes.document;

import static org.springframework.data.elasticsearch.annotations.DateFormat.*;

import java.time.LocalDateTime;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.GenreType;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
