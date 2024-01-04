package com.pgms.apievent.scheduler;

import com.pgms.coreinfraes.buffer.DocumentBuffer;
import com.pgms.coreinfraes.repository.EventSearchQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventDocumentScheduler {

    private static final int SCHEDULE_UPDATE_CYCLE = 30000;

    private final EventSearchQueryRepository eventSearchQueryRepository;

    @Scheduled(fixedDelay = SCHEDULE_UPDATE_CYCLE)
    public void scheduleUpdateEventDocument() {
        log.info(">>>>> execute scheduleUpdateEventDocument");

        List<Object> documents = DocumentBuffer.getAll();
        DocumentBuffer.deleteAll();
        
        eventSearchQueryRepository.bulkUpdate(documents);
    }
}
