package com.pgms.coreinfraes.document;

import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.GenreType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(indexName = "event")
public class EventDocument {
    @Id
    @Field(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Field(name = "title")
    private String title;

    @Field(name = "description")
    private String description;

    @Field(name = "running_time")
    private int runningTime;

    @Field(name = "started_at")
    private LocalDateTime startedAt;

    @Field(name = "ended_at")
    private LocalDateTime endedAt;

    @Field(name = "rating")
    private String rating;

    @Field(name = "genre")
    @Enumerated(value = EnumType.STRING)
    private GenreType genreType;

    @Field(name = "average_score")
    private Double averageScore = 0.0;

    @Field(name = "thumbnail")
    private String thumbnail;

    @Field(name = "booking_started_at")
    private LocalDateTime bookingStartedAt;

    @Field(name = "booking_ended_at")
    private LocalDateTime bookingEndedAt;

    @Field(name = "event_hall")
    private EventHall eventHall;

    public static EventDocument from(Event event) {
        return EventDocument.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .runningTime(event.getRunningTime())
                .startedAt(event.getStartedAt())
                .endedAt(event.getEndedAt())
                .rating(event.getRating())
                .genreType(event.getGenreType())
                .averageScore(event.getAverageScore())
                .thumbnail(event.getThumbnail())
                .bookingStartedAt(event.getBookingStartedAt())
                .bookingEndedAt(event.getBookingEndedAt())
                .eventHall(event.getEventHall())
                .build();
    }
}
