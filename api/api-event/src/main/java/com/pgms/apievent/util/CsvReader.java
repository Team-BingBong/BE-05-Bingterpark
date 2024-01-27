package com.pgms.apievent.util;

import static com.pgms.apievent.exception.EventErrorCode.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pgms.apievent.event.dto.request.EventCreateRequest;
import com.pgms.apievent.exception.EventException;
import com.pgms.coredomain.domain.event.Event;
import com.pgms.coredomain.domain.event.EventHall;
import com.pgms.coredomain.domain.event.GenreType;
import com.pgms.coredomain.domain.event.repository.EventHallRepository;
import com.pgms.coredomain.domain.event.repository.EventRepository;
import com.pgms.coreinfraes.document.EventDocument;
import com.pgms.coreinfraes.repository.EventSearchQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CsvReader {

	private final EventRepository eventRepository;
	private final EventSearchQueryRepository eventSearchQueryRepository;
	private final EventHallRepository eventHallRepository;
	private final JdbcTemplateEventRepository jdbcTemplateEventRepository;

	public void saveEventCsv(String filepath) {
		try {
			String filePath = Paths.get(filepath).toString();
			EventHall eventHall = eventHallRepository.findById(1L)
				.orElseThrow(() -> new EventException(EVENT_HALL_NOT_FOUND));

			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(filePath), "EUC-KR"))) {
				List<Event> events = br.lines()
					.skip(1) // Skip header line
					.map(line -> line.split(","))
					.map(data -> new EventCreateRequest(
						data[0], data[2], 100,
						LocalDateTime.now(), LocalDateTime.now().plusDays(10),
						null, GenreType.MUSICAL,
						LocalDateTime.now(), LocalDateTime.now().plusDays(10), 1L)
						.toEntity(eventHall))
					.collect(Collectors.toList());

				jdbcTemplateEventRepository.bulkSave(events);
			} catch (IOException e) {
				throw new RuntimeException("Failed to load file", e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void saveAllDocument() {
		int batchSize = 100;
		List<Event> events = eventRepository.findAll();

		List<EventDocument> documents = new ArrayList<>();

		for (int i = 0; i < events.size(); i += batchSize) {
			List<Event> batchEvents = events.subList(i, Math.min(i + batchSize, events.size()));
			batchEvents.forEach(e -> documents.add(EventDocument.from(e)));
			eventSearchQueryRepository.bulkInsert(documents);
			documents.clear();
		}
	}
}
