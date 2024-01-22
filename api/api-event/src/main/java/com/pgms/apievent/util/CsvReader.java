package com.pgms.apievent.util;

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
import com.pgms.apievent.event.service.EventService;
import com.pgms.coredomain.domain.event.GenreType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvReader {
    private final EventService eventService;

    public void saveEventCsv(String filepath) {
        try{
            String filePath = Paths.get(filepath).toString();
            List<String[]> datas = loadFile(filePath);

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

                    eventService.createEvent(request);
                }
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private  List<String[]> loadFile(String file_path) {
        List<String[]> infoList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file_path), "EUC-KR"))) {
            String line;
            while((line = br.readLine()) != null){
                String[] data = line.split(",");
                infoList.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("can not load file!!");
        }
        return infoList;
    }
}
