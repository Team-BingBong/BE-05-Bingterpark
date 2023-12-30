package com.pgms.coreinfraes.document;

import com.pgms.coredomain.domain.event.Event;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(indexName = "event_test")
public class EventDocument {

    @Id
    private Long id;

//    @Field(name = "title", type = FieldType.Text)
//    private String title;

//    @Field(name = "description", type = FieldType.Text)
//    private String description;

//    @Field(name = "running_time", type = FieldType.Short)
//    private int runningTime;

//    @Field(name = "started_at", type = FieldType.Date)
//    private LocalDateTime startedAt;

//    @Field(name = "ended_at", type = FieldType.Date)
//    private LocalDateTime endedAt;

//    @Field(name = "rating", type = FieldType.Keyword)
//    private String rating;

//    @Field(name = "genre", type = FieldType.Keyword)
//    @Enumerated(value = EnumType.STRING)
//    private GenreType genreType;

//    @Field(name = "average_score", type = FieldType.Double)
//    private Double averageScore = 0.0;

//    @Field(name = "booking_started_at", type = FieldType.Date)
//    private LocalDateTime bookingStartedAt;
//
//    @Field(name = "booking_ended_at", type = FieldType.Date)
//    private LocalDateTime bookingEndedAt;

//    @Field(name = "event_hall", type = FieldType.Object)
//    private EventHall eventHall;

    public static EventDocument from(Event event) {
        return EventDocument.builder()
                .id(event.getId())
//                .title(event.getTitle())
//                .description(event.getDescription())
//                .runningTime(event.getRunningTime())
//                .startedAt(event.getStartedAt())
//                .endedAt(event.getEndedAt())
//                .rating(event.getRating())
//                .genreType(event.getGenreType())
//                .averageScore(event.getAverageScore())
//                .bookingStartedAt(event.getBookingStartedAt())
//                .bookingEndedAt(event.getBookingEndedAt())
//                .eventHall(event.getEventHall())
                .build();
    }
}
