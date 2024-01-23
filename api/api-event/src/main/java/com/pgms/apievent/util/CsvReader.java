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

import org.springframework.stereotype.Service;

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
public class CsvReader {

	private final EventRepository eventRepository;
	private final EventSearchQueryRepository eventSearchQueryRepository;
	private final EventHallRepository eventHallRepository;

	public void saveEventCsv(String filepath) {
		try {
			String filePath = Paths.get(filepath).toString();
			List<String[]> datas = loadFile(filePath);
			List<EventDocument> documents = new ArrayList<>();
			List<Event> events = new ArrayList<>();

			datas.forEach(data -> {
					EventCreateRequest request = new EventCreateRequest(
						data[0],
						data[2],
						100,
						LocalDateTime.now(),
						LocalDateTime.now().plusDays(10),
						null,
						GenreType.MUSICAL,
						LocalDateTime.now(),
						LocalDateTime.now().plusDays(10),
						1L);

					EventHall eventHall = eventHallRepository.findById(request.eventHallId())
						.orElseThrow(() -> new EventException(EVENT_HALL_NOT_FOUND));
					Event event = request.toEntity(eventHall);

					events.add(event);
				}
			);
			List<Event> savedEvents = eventRepository.saveAll(events);
			savedEvents.forEach(e -> {
				EventDocument eventDocument = EventDocument.from(e);
				documents.add(eventDocument);
			});
			eventSearchQueryRepository.bulkInsert(documents);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<String[]> loadFile(String file_path) {
		List<String[]> infoList = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_path), "EUC-KR"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				infoList.add(data);
			}
		} catch (IOException e) {
			throw new RuntimeException("can not load file!!");
		}
		return infoList;
	}
}
