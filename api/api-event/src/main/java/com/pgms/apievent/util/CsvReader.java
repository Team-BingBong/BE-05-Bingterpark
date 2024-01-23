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

	public void saveEventCsv(String filepath) {
		try {
			String filePath = Paths.get(filepath).toString();
			List<String[]> datas = loadFile(filePath);
			List<EventDocument> documents = new ArrayList<>();
			List<Event> events = new ArrayList<>();

			int count = 0;

			EventHall eventHall = eventHallRepository.findById(1L)
				.orElseThrow(() -> new EventException(EVENT_HALL_NOT_FOUND));
			for(String[] data : datas) {
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

				Event event = request.toEntity(eventHall);

				events.add(event);
				count++;
				if(count == 100){
					count = 0;
					List<Event> savedEvents = eventRepository.saveAll(events);
					savedEvents.forEach(e -> {
						EventDocument eventDocument = EventDocument.from(e);
						documents.add(eventDocument);
					});
					eventSearchQueryRepository.bulkInsert(documents);
					events.clear();
					documents.clear();
				}
			}


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
