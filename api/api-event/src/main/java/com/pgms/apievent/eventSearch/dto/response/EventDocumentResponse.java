package com.pgms.apievent.eventSearch.dto.response;

import com.pgms.coreinfraes.document.EventDocument;

public record EventDocumentResponse(
	Long id,
	String title,
	String description,
	String startedAt,
	String endedAt,
	String viewRating,
	String genreType,
	Double average,
	Long eventHallId
) {
	public static EventDocumentResponse of(EventDocument eventDocument) {
		return new EventDocumentResponse(
			eventDocument.getId(),
			eventDocument.getTitle(),
			eventDocument.getDescription(),
			eventDocument.getStartedAt().toString(),
			eventDocument.getEndedAt().toString(),
			eventDocument.getViewRating(),
			eventDocument.getGenreType().toString(),
			eventDocument.getAverageScore(),
			eventDocument.getEventHallId()
		);
	}
}
